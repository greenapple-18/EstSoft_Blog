package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.CommentResponse;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @InjectMocks
    CommentController commentController;

    @Mock
    CommentService commentService;

    @Mock
    BlogService blogService;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();
    }

    @Test
    void testWriteComment() throws Exception {
        // given
        Article article = new Article();
        Comment comment = new Comment(article, "Test Comment");
        comment.setCreatedAt(LocalDateTime.now());

        Mockito.when(commentService.saveComment(any(), any()))
                .thenReturn(comment);

        String requestJson = """
            {
                "body": "Test Comment"
            }
        """;

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/articles/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.body").value(comment.getBody()));
    }

    @Test
    void testFindById() throws Exception {
        // given
        Long commentId = 1L;
        Article article = new Article();
        Comment comment = new Comment(article, "Test Comment");
        comment.setCreatedAt(LocalDateTime.now());

        Mockito.when(commentService.findById(any()))
                .thenReturn(comment);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(comment.getBody()));
    }

    @Test
    void testUpdateById() throws Exception {
        // given
        Long commentId = 1L;
        Article article = new Article();

        Comment comment = new Comment(article, "Test");
        comment.setCreatedAt(LocalDateTime.now());
        comment.update("Updated Body");
        Mockito.when(commentService.update(any(), any()))
                .thenReturn(new CommentResponse(comment));

        String requestJson = """
            {
                "body": "Updated Body"
            }
        """;

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.body").value(comment.getBody()));
    }

    @Test
    void testDeleteById() throws Exception {
        Long commentId = 1L;


        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());

        verify(commentService, times(1)).deleteById(any());
    }

    @Test
    void testFindAllComment() throws Exception {
        // given
        Long articleId = 1L;

        Article article = new Article();
        article.setId(articleId);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        Comment comment1 = new Comment(article, "Test Comment1");
        comment1.setCreatedAt(LocalDateTime.now());

        Comment comment2 = new Comment(article, "Test Comment2");
        comment2.setCreatedAt(LocalDateTime.now());

        List<Comment> comments = List.of(comment1, comment2);

        Mockito.when(blogService.findById(any()))
                .thenReturn(article);

        Mockito.when(commentService.findAll(any()))
                .thenReturn(comments);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/{articleId}/comments", articleId)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.articleId").value(articleId))
                .andExpect(jsonPath("$.commentList[0].body").value(comment1.getBody()))
                .andExpect(jsonPath("$.commentList[1].body").value(comment2.getBody()));
    }
}