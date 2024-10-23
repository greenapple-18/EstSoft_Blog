package com.estsoft.springproject.bookproject.repository;

import com.estsoft.springproject.bookproject.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, String> {
}
