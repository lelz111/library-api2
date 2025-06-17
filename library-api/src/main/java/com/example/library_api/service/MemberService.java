package com.example.library_api.service;

import com.example.library_api.dto.MemberRequestDto;
import com.example.library_api.dto.MemberResponseDto;
import com.example.library_api.model.Member;
import com.example.library_api.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public MemberResponseDto createMember(MemberRequestDto dto) {
        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Member member = new Member();
        member.setName(dto.getName());
        member.setEmail(dto.getEmail());

        Member saved = memberRepository.save(member);

        return mapToDto(saved);
    }

    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return mapToDto(member);
    }

    public List<String> getBorrowedBooks(Long memberId) {
        // Mocked karena tidak ada relasi, implementasi asli perlu query custom
        return List.of("Sample Book 1", "Sample Book 2");
    }

    private MemberResponseDto mapToDto(Member member) {
        MemberResponseDto dto = new MemberResponseDto();
        dto.setId(member.getId());
        dto.setName(member.getName());
        dto.setEmail(member.getEmail());
        return dto;
    }
}
