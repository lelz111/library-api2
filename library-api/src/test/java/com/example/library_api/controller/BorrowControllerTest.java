package com.example.library_api.controller;

import com.example.library_api.dto.BorrowRequestDto;
import com.example.library_api.dto.BorrowResponseDto;
import com.example.library_api.service.BorrowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BorrowControllerTest {

    @InjectMocks
    private BorrowController borrowController;

    @Mock
    private BorrowService borrowService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(borrowController).build();
    }

    @Test
    void testBorrowBook() throws Exception {
        BorrowRequestDto request = BorrowRequestDto.builder().bookId(1L).memberId(2L).build();
        BorrowResponseDto response = BorrowResponseDto.builder()
                .id(1L).bookId(1L).memberId(2L).borrowDate(LocalDate.now()).build();

        when(borrowService.borrowBook(any())).thenReturn(response);

        mockMvc.perform(post("/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"book_id\":1, \"member_id\":2}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testGetByMember() throws Exception {
        when(borrowService.getBorrowedBooksByMemberId(2L))
                .thenReturn(List.of(BorrowResponseDto.builder().id(1L).bookId(10L).memberId(2L).borrowDate(LocalDate.now()).build()));

        mockMvc.perform(get("/borrow/member/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].book_id").value(10));
    }
}
