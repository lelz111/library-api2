package com.example.library_api.service;

import com.example.library_api.dto.MemberRequestDto;
import com.example.library_api.dto.MemberResponseDto;
import com.example.library_api.model.Member;
import com.example.library_api.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createMember_success() {
        MemberRequestDto dto = new MemberRequestDto();
        dto.setName("John");
        dto.setEmail("john@example.com");

        when(memberRepository.existsByEmail("john@example.com")).thenReturn(false);

        Member savedMember = Member.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .build();

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponseDto response = memberService.createMember(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("John", response.getName());
        assertEquals("john@example.com", response.getEmail());

        verify(memberRepository).existsByEmail("john@example.com");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void createMember_emailAlreadyExists() {
        MemberRequestDto dto = new MemberRequestDto();
        dto.setName("Jane");
        dto.setEmail("jane@example.com");

        when(memberRepository.existsByEmail("jane@example.com")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            memberService.createMember(dto);
        });

        assertEquals("Email already exists", ex.getMessage());
        verify(memberRepository, never()).save(any());
    }

    @Test
    void getAllMembers_success() {
        List<Member> members = Arrays.asList(
                Member.builder().id(1L).name("A").email("a@mail.com").build(),
                Member.builder().id(2L).name("B").email("b@mail.com").build()
        );

        when(memberRepository.findAll()).thenReturn(members);

        List<MemberResponseDto> result = memberService.getAllMembers();

        assertEquals(2, result.size());
        assertEquals("A", result.get(0).getName());
        assertEquals("b@mail.com", result.get(1).getEmail());

        verify(memberRepository).findAll();
    }

    @Test
    void getMemberById_success() {
        Member member = Member.builder().id(10L).name("C").email("c@mail.com").build();
        when(memberRepository.findById(10L)).thenReturn(Optional.of(member));

        MemberResponseDto result = memberService.getMemberById(10L);

        assertEquals("C", result.getName());
        assertEquals("c@mail.com", result.getEmail());

        verify(memberRepository).findById(10L);
    }

    @Test
    void getMemberById_notFound() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            memberService.getMemberById(99L);
        });

        assertEquals("Member not found", ex.getMessage());
        verify(memberRepository).findById(99L);
    }

    @Test
    void getBorrowedBooks_dummyData() {
        List<String> result = memberService.getBorrowedBooks(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains("Sample Book 1"));
        assertTrue(result.contains("Sample Book 2"));
    }
}
