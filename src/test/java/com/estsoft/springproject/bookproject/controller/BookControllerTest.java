package com.estsoft.springproject.bookproject.controller;

import com.estsoft.springproject.bookproject.domain.Book;
import com.estsoft.springproject.bookproject.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    MockMvc mockMvc;

    @Mock
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Disabled
    @Test
    public void testAddBook() throws Exception{
        Book newBook = new Book("book1", "author1");

        ResultActions resultActions = mockMvc.perform(post("/books").accept(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].name").value(newBook.getId()))
                .andExpect(jsonPath("$[0].author").value(newBook.getName()));
    }

    @Test
    public void testShowAll() throws Exception {
        List<Book> mockBooks = Arrays.asList(
                new Book("Book 1", "Author 1"),
                new Book("Book 2", "Author 2"),
                new Book("Book 3", "Author 3")
        );

        Mockito.when(bookService.findAll()).thenReturn(mockBooks);

        ResultActions resultActions = mockMvc.perform(get("/books"));

        resultActions.andExpect(status().isOk())
                .andExpect(view().name("bookManagement"));
    }
}