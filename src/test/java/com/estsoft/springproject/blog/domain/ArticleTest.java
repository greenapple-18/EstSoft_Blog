package com.estsoft.springproject.blog.domain;

import org.junit.jupiter.api.Test;

class ArticleTest {
    @Test
    public void test() {
        Article article = new Article("제목", "내용");

        Article articleBuilder = Article.builder()
                .title("제목")
                .content("내용")
                .build();
    }

}