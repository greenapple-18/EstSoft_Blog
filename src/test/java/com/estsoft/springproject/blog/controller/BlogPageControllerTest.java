package com.estsoft.springproject.blog.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@ExtendWith(MockitoExtension.class)
class BlogPageControllerTest {
    @InjectMocks
    BlogPageController blogPageController;

    @Mock
    BlogService blogService;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(blogPageController).build();
    }

    @Test
    void testGetArticles() throws Exception {
        List<Article> articles = List.of(
                new Article("제목1", "내용1"),
                new Article("제목2", "내용2")
        );
        when(blogService.findAll()).thenReturn(articles);

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articleList"))
                .andExpect(model().attributeExists("articles"));
    }

    @Test
    void testShowArticle() throws Exception {
        Long articleId = 1L;
        Article article = new Article("제목1", "내용1");

        when(blogService.findById(articleId)).thenReturn(article);

        mockMvc.perform(get("/articles/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(view().name("article"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void testNewArticle() throws Exception {

        mockMvc.perform(get("/new-article"))
                .andExpect(status().isOk())
                .andExpect(view().name("newArticle"))
                .andExpect(model().attributeExists("article"));
    }

    @Test
    void testEditArticle() throws Exception {

        Long articleId = 1L;
        Article article = new Article("제목1", "내용1");
        when(blogService.findById(articleId)).thenReturn(article);

        mockMvc.perform(get("/new-article").param("id", String.valueOf(articleId)))
                .andExpect(status().isOk())
                .andExpect(view().name("newArticle"))
                .andExpect(model().attributeExists("article"));
    }
}