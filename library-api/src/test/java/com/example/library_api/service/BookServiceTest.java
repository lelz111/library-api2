package com.example.library_api.service;

import com.example.library_api.dto.BookRequestDto;
import com.example.library_api.dto.BookResponseDto;
import com.example.library_api.model.Book;
import com.example.library_api.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBook_success() {
        BookRequestDto dto = new BookRequestDto();
        dto.setTitle("Spring Boot");
        dto.setStock(10);

        Book savedBook = Book.builder()
                .id(1L)
                .title("Spring Boot")
                .stock(10)
                .build();

        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        BookResponseDto response = bookService.createBook(dto);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Spring Boot", response.getTitle());
        assertEquals(10, response.getStock());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void getAllBooks_success() {
        List<Book> books = Arrays.asList(
                Book.builder().id(1L).title("Book A").stock(5).build(),
                Book.builder().id(2L).title("Book B").stock(7).build()
        );

        when(bookRepository.findAll()).thenReturn(books);

        List<BookResponseDto> result = bookService.getAllBooks();

        assertEquals(2, result.size());
        assertEquals("Book A", result.get(0).getTitle());
        assertEquals(7, result.get(1).getStock());
        verify(bookRepository).findAll();
    }

    @Test
    void getBookById_success() {
        Book book = Book.builder().id(1L).title("Java").stock(3).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        BookResponseDto response = bookService.getBookById(1L);

        assertEquals("Java", response.getTitle());
        assertEquals(3, response.getStock());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_notFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.getBookById(1L);
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findById(1L);
    }

    @Test
    void decreaseStock_success() {
        Book book = Book.builder().id(1L).title("Java").stock(5).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        bookService.decreaseStock(1L);

        assertEquals(4, book.getStock());  // mutable
        verify(bookRepository).save(book);
    }

    @Test
    void decreaseStock_outOfStock() {
        Book book = Book.builder().id(1L).title("Java").stock(0).build();
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.decreaseStock(1L);
        });

        assertEquals("Book out of stock", exception.getMessage());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void decreaseStock_bookNotFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.decreaseStock(99L);
        });

        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository).findById(99L);
    }
}
