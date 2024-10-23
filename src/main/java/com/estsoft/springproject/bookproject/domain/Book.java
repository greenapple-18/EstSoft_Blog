package com.estsoft.springproject.bookproject.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Column(name = "id", updatable = false)
    private String id;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String name;

    @Column(columnDefinition = "VARCHAR(255) NOT NULL")
    private String author;

    @Builder
    public Book(String name, String author) {
        this.name = name;
        this.author = author;
    }

    public BookDTO convert() {
        return new BookDTO(id, name, author);
    }
}
