package com.estsoft.springproject.bookproject.service;

import com.estsoft.springproject.bookproject.domain.Book;
import com.estsoft.springproject.bookproject.repository.BookRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository repository;


    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll() {
        return repository.findAll(Sort.by("id"));
    }

    public Book findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found id: " + id));
    }

    public Book save(Book book) {
        return repository.save(book);
    }
}