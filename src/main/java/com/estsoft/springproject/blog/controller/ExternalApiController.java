package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.CommentRequest;
import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/external")
public class ExternalApiController {

    private final BlogService blogService;
    private final CommentService commentService;

    public ExternalApiController(BlogService blogService, CommentService commentService) {
        this.blogService = blogService;
        this.commentService = commentService;
    }

    @GetMapping
    public String callApi() {
        RestTemplate restTemplate = new RestTemplate();

        String url = "https://jsonplaceholder.typicode.com/posts";

        ResponseEntity<List<Content>> resultList = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Content>>() {
        });
        log.info("code: {}", resultList.getStatusCode());
        log.info("{}", resultList.getBody());

        return "OK";
    }

    @GetMapping("/articles")
    public String callArticlesApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/posts";

        ResponseEntity<List<AddArticleRequest>> resultList = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AddArticleRequest>>() {
                }
        );

        resultList.getBody().forEach(blogService::saveArticle);

        return "OK";
    }

    @GetMapping("/comments")
    public String callCommentsApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://jsonplaceholder.typicode.com/comments";

        ResponseEntity<List<CommentRequest>> resultList = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CommentRequest>>() {
                }
        );

        // 댓글 데이터 저장
        resultList.getBody().forEach(commentResponse -> {
            Long articleId = commentResponse.getArticleId(); // JSON에서 받은 postId를 articleId로 매핑
            String body = commentResponse.getBody(); // 댓글 내용
            CommentRequest commentRequest = new CommentRequest();
            commentRequest.setArticleId(articleId);
            commentRequest.setBody(body);
            commentService.saveComment(articleId, commentResponse); // 댓글 저장
        });

        return "OK";
    }

}
