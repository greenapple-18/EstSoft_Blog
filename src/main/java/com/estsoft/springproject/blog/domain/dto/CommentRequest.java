package com.estsoft.springproject.blog.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CommentRequest {
    private Long id;
    private String body;
    private String createdAt;
    private ArticleResponse article;
    @JsonProperty("postId")
    private Long ArticleId;
}
