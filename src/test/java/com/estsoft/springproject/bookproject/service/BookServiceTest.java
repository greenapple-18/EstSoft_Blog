package com.estsoft.springproject.bookproject.service;

import com.estsoft.springproject.bookproject.domain.Book;
import com.estsoft.springproject.bookproject.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Given
        List<Book> books = Arrays.asList(
                new Book("1", "Book One", "Author One"),
                new Book("2", "Book Two", "Author Two")
        );
        when(bookRepository.findAll(Sort.by("id"))).thenReturn(books);

        // When
        List<Book> result = bookService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo("1");
        assertThat(result.get(1).getId()).isEqualTo("2");
        verify(bookRepository, times(1)).findAll(Sort.by("id"));
    }

    @Test
    void testFindById() {
        // Given
        String bookId = "1";
        Book book = new Book(bookId, "Book One", "Author One");
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        // When
        Book result = bookService.findById(bookId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookId);
        assertThat(result.getName()).isEqualTo("Book One");
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testFindById_Exception() {
        // Given
        String bookId = "99";
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> bookService.findById(bookId));
        assertThat(exception.getMessage()).isEqualTo("not found id: " + bookId);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testSaveBook() {
        // Given
        Book book = new Book("1", "Book One", "Author One");
        when(bookRepository.save(book)).thenReturn(book);

        // When
        Book result = bookService.save(book);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Book One");
        verify(bookRepository, times(1)).save(book);
    }
}
