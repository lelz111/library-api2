package com.example.library_api.service;

import com.example.library_api.dto.BorrowRequestDto;
import com.example.library_api.dto.BorrowResponseDto;
import com.example.library_api.model.Borrow;
import com.example.library_api.repository.BorrowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BorrowServiceTest {

    @InjectMocks
    private BorrowService borrowService;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookService bookService;

    @Mock
    private MemberService memberService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void borrowBook_success() {
        // Arrange
        BorrowRequestDto dto = BorrowRequestDto.builder()
                .bookId(1L)
                .memberId(2L)
                .build();

        Borrow savedBorrow = Borrow.builder()
                .id(1L)
                .bookId(1L)
                .memberId(2L)
                .borrowDate(LocalDate.now())
                .build();

        when(borrowRepository.save(any(Borrow.class))).thenReturn(savedBorrow);

        // Act
        BorrowResponseDto response = borrowService.borrowBook(dto);

        // Assert
        assertEquals(1L, response.getId());
        assertEquals(1L, response.getBookId());
        assertEquals(2L, response.getMemberId());
        verify(bookService, times(1)).decreaseStock(1L);
        verify(borrowRepository).save(any(Borrow.class));
    }

    @Test
    void getBorrowedBooksByMemberId_success() {
        // Arrange
        List<Borrow> mockList = List.of(
                Borrow.builder()
                        .id(1L)
                        .bookId(101L)
                        .memberId(1L)
                        .borrowDate(LocalDate.of(2024, 1, 10))
                        .build()
        );

        when(borrowRepository.findByMemberId(1L)).thenReturn(mockList);

        // Act
        List<BorrowResponseDto> result = borrowService.getBorrowedBooksByMemberId(1L);

        // Assert
        assertEquals(1, result.size());
        assertEquals(101L, result.get(0).getBookId());
        assertEquals(1L, result.get(0).getMemberId());
        assertEquals(LocalDate.of(2024, 1, 10), result.get(0).getBorrowDate());
    }
}
