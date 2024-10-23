package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.CommentRequest;
import com.estsoft.springproject.blog.domain.dto.CommentInArticle;
import com.estsoft.springproject.blog.domain.dto.CommentResponse;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService service;
    private final BlogService blogService;

    public CommentController(CommentService service, BlogService blogService) {
        this.service = service;
        this.blogService = blogService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentResponse> writeComment(@PathVariable Long articleId, @RequestBody CommentRequest request) {
        Comment comment = service.saveComment(articleId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new CommentResponse(comment));
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long commentId) {
        return ResponseEntity.ok(new CommentResponse(service.findById(commentId)));
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateById(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        CommentResponse updateComment = service.update(commentId, request);
        return ResponseEntity.ok(updateComment);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteById(@PathVariable Long commentId) {
        service.deleteById(commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<CommentInArticle> findAllComment(@PathVariable Long articleId){
        return ResponseEntity.ok(new CommentInArticle(blogService.findById(articleId), service.findAll(articleId)));
    }
}
