package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.dto.*;
import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "블로그 저장/삭제/수정/조회용 API")
@Slf4j
@RestController
@RequestMapping("/api")
public class BlogController {

    private final BlogService service;

    public BlogController(BlogService service) {
        this.service = service;
    }

    @PostMapping("/articles")
    public ResponseEntity<ArticleResponse> writeArticle(@RequestBody AddArticleRequest request) {
        log.info("request: {}, {}", request.getTitle(), request.getContent());
        Article article = service.saveArticle(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(article.convert());
    }

    @Operation(summary = "블로그 전체 목록 보기", description = "블로그 메인 화면에서 보여주는 전체 목록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "100", description = "요청에 성공했습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/articles")
    public ResponseEntity<List<ArticleResponse>> findAll() {
        List<ArticleResponse> articleResponseList = service.findAll().stream()
                .map(Article::convert)
                .toList();
        return ResponseEntity.ok(articleResponseList);
    }

    @Parameter(name = "id", description = "블로그 글 ID", example = "45")
    @GetMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> findById(@PathVariable Long id) {
//        Article article = service.findById(id);
//        ArticleResponse articleResponse = new ArticleResponse(article.getId(), article.getTitle(), article.getContent());
        return ResponseEntity.ok(service.findById(id).convert());
    }

    @DeleteMapping("/articles/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/articles/{id}")
    public ResponseEntity<ArticleResponse> updateById(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {
        Article updateArticle = service.update(id, request);
        return ResponseEntity.ok(updateArticle.convert());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(illegalArgumentException.getMessage());
    }


}
