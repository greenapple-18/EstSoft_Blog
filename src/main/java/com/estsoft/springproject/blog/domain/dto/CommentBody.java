package com.estsoft.springproject.blog.domain.dto;

import com.estsoft.springproject.blog.domain.Comment;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class CommentBody {
    private final Long commentId;
    private final Long articleId;
    private final String body;
    private final String createdAt;

    CommentBody(Comment comment){
        this.commentId = comment.getId();
        this.articleId = comment.getArticle().getId();
        this.body = comment.getBody();
        this.createdAt = comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
