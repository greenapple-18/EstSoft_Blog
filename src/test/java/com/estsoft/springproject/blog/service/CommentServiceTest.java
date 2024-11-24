package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.CommentRequest;
import com.estsoft.springproject.blog.domain.dto.CommentResponse;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private CommentRepository commentRepository;

    @Test
    void testSaveComment() {
        // given
        Long articleId = 1L;
        CommentRequest request = new CommentRequest();
        request.setBody("This is a comment.");
        request.setArticleId(articleId);

        Article article = new Article("Title", "Content");
        Comment savedComment = new Comment(article, "This is a comment.");

        // stub
        when(blogRepository.findById(articleId)).thenReturn(Optional.of(article));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        // when
        Comment result = commentService.saveComment(articleId, request);

        // then
        assertNotNull(result);
        assertEquals("This is a comment.", result.getBody());
        verify(blogRepository, times(1)).findById(articleId);
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testFindById() {
        // given
        Long commentId = 1L;
        Comment comment = new Comment(new Article("Title", "Content"), "Comment body");

        // stub
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        Comment result = commentService.findById(commentId);

        // then
        assertNotNull(result);
        assertEquals("Comment body", result.getBody());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testUpdate() {
        // given
        Long commentId = 1L;
        CommentRequest request = new CommentRequest();
        request.setBody("Updated comment body");

        Comment existingComment = new Comment(new Article("Title", "Content"), "Old comment body");
        existingComment.setCreatedAt(LocalDateTime.now());

        // stub
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // when
        CommentResponse response = commentService.update(commentId, request);

        // then
        assertNotNull(response);
        assertEquals("Updated comment body", response.getBody());
        verify(commentRepository, times(1)).findById(commentId);
    }

    @Test
    void testDeleteById() {
        // given
        Long commentId = 1L;

        // when
        commentService.deleteById(commentId);

        // then
        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testFindAll() {
        // given
        Long articleId = 1L;
        Article article = new Article("Title", "Content");
        List<Comment> comments = Arrays.asList(
                new Comment(article, "Comment 1"),
                new Comment(article, "Comment 2")
        );

        // stub
        when(commentRepository.findByArticleId(articleId)).thenReturn(comments);

        // when
        List<Comment> result = commentService.findAll(articleId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getBody());
        assertEquals("Comment 2", result.get(1).getBody());
        verify(commentRepository, times(1)).findByArticleId(articleId);
    }
}
