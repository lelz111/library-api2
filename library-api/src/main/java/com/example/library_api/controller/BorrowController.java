package com.example.library_api.controller;

import com.example.library_api.dto.BorrowRequestDto;
import com.example.library_api.dto.BorrowResponseDto;
import com.example.library_api.service.BorrowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping
    public BorrowResponseDto borrowBook(@Valid @RequestBody BorrowRequestDto dto) {
        return borrowService.borrowBook(dto);
    }

    @GetMapping("/member/{id}")
    public List<BorrowResponseDto> getByMember(@PathVariable("id") Long memberId) {
        return borrowService.getBorrowedBooksByMemberId(memberId);
    }
}
