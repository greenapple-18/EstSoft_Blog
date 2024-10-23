package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.Comment;
import com.estsoft.springproject.blog.domain.dto.CommentRequest;
import com.estsoft.springproject.blog.domain.dto.CommentResponse;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final BlogRepository blogRepository;
    private final CommentRepository commentRepository;

    public CommentService(BlogRepository blogRepository, CommentRepository commentRepository) {
        this.blogRepository = blogRepository;
        this.commentRepository = commentRepository;
    }

    public Comment saveComment(Long articleId, CommentRequest request) {
        Article article = blogRepository.findById(articleId).orElseThrow();
        return commentRepository.save(new Comment(article, request.getBody()));
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElse(new Comment());
    }

    @Transactional
    public CommentResponse update(Long id, CommentRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found id: " + id));
        comment.update(request.getBody());
        return new CommentResponse(comment);
    }

    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    public List<Comment> findAll(Long articleId) {
        return commentRepository.findByArticleId(articleId);
    }
}
