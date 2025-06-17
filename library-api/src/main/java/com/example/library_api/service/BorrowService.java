package com.example.library_api.service;

import com.example.library_api.dto.BorrowRequestDto;
import com.example.library_api.dto.BorrowResponseDto;
import com.example.library_api.model.Borrow;
import com.example.library_api.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    @Autowired private BorrowRepository borrowRepository;
    @Autowired private BookService bookService;

    public BorrowResponseDto borrowBook(BorrowRequestDto dto) {
        bookService.decreaseStock(dto.getBookId());

        Borrow borrow = Borrow.builder()
                .bookId(dto.getBookId())
                .memberId(dto.getMemberId())
                .borrowDate(LocalDate.now())
                .build();

        Borrow saved = borrowRepository.save(borrow);

        return mapToDto(saved);
    }

    public List<BorrowResponseDto> getBorrowedBooksByMemberId(Long memberId) {
        return borrowRepository.findByMemberId(memberId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private BorrowResponseDto mapToDto(Borrow borrow) {
        return BorrowResponseDto.builder()
                .id(borrow.getId())
                .bookId(borrow.getBookId())
                .memberId(borrow.getMemberId())
                .borrowDate(borrow.getBorrowDate())
                .build();
    }
}
