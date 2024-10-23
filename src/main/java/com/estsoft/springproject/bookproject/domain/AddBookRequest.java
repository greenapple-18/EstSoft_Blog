package com.estsoft.springproject.bookproject.domain;

public class AddBookRequest {
    private String name;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .name(this.name)
                .author(this.author)
                .build();
    }
}
