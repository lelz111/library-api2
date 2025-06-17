package com.example.library_api.controller;

import com.example.library_api.dto.MemberRequestDto;
import com.example.library_api.dto.MemberResponseDto;
import com.example.library_api.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberControllerTest {

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void testCreateMember() throws Exception {
        MemberRequestDto request = new MemberRequestDto("John Doe", "john@example.com");
        MemberResponseDto response = new MemberResponseDto(1L, "John Doe", "john@example.com");

        when(memberService.createMember(any())).thenReturn(response);

        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCreateMemberValidationError() throws Exception {
        // Missing name
        mockMvc.perform(post("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john@example.com\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetAllMembers() throws Exception {
        when(memberService.getAllMembers())
                .thenReturn(List.of(new MemberResponseDto(1L, "John Doe", "john@example.com")));

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    @Test
    void testGetAllMembersEmpty() throws Exception {
        when(memberService.getAllMembers()).thenReturn(List.of());

        mockMvc.perform(get("/members"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetMemberById() throws Exception {
        when(memberService.getMemberById(1L))
                .thenReturn(new MemberResponseDto(1L, "Jane", "jane@example.com"));

        mockMvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("jane@example.com"));
    }

    @Test
    void testGetMemberByIdNotFound() throws Exception {
        when(memberService.getMemberById(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/members/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetBorrowedBooks() throws Exception {
        when(memberService.getBorrowedBooks(1L))
                .thenReturn(List.of("Book A", "Book B"));

        mockMvc.perform(get("/members/1/borrowed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Book A"));
    }

    @Test
    void testGetBorrowedBooksEmpty() throws Exception {
        when(memberService.getBorrowedBooks(2L)).thenReturn(List.of());

        mockMvc.perform(get("/members/2/borrowed"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetBorrowedBooksNotFound() throws Exception {
        when(memberService.getBorrowedBooks(99L)).thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(get("/members/99/borrowed"))
                .andExpect(status().isInternalServerError());
    }
}