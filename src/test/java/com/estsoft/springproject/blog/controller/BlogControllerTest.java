package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Disabled
@AutoConfigureMockMvc
@SpringBootTest
class BlogControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogRepository repository;

    @Autowired
    private BlogService blogService;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        repository.deleteAll();
    }

    @Test
    public void addArticle() throws Exception {
        Article article = new Article("제목", "내용");
        repository.save(article);
        String json = objectMapper.writeValueAsString(article);

        ResultActions resultActions = mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(article.getTitle()))
                .andExpect(jsonPath("$.content").value(article.getContent()));
    }

    @Test
    public void findAll() throws Exception {
        // given : 조회 API에 필요한 값 세팅
        Article article = repository.save(new Article("title", "content"));


        // when : 조회 API
        ResultActions resultActions = mockMvc.perform(get("/articles")
                .accept(MediaType.APPLICATION_JSON));
        // then : API 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value(article.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article.getContent()));
    }

    @Test
    public void findById() throws Exception {
        // given : data insert
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        // when : API 호출
        ResultActions resultActions = mockMvc.perform(get("/articles/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        // then : API 호출 결과 검증
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(article.getTitle()))
                .andExpect(jsonPath("$.content").value(article.getContent()))
                .andExpect(jsonPath("$.id").value(article.getId()));

    }

    // 단건 조회 실패 API
    @Test
    public void findOneException() throws Exception {
        // when
        ResultActions resultActions = mockMvc.perform(get("/articles/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isBadRequest());

        assertThrows(IllegalArgumentException.class, () -> blogService.findById(1L));
    }

    @Test
    public void deleteById() throws Exception {
        // given
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        // when
        ResultActions resultActions = mockMvc.perform(delete("/articles/{id}", id)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk());
        assertThat(repository.existsById(id)).isFalse();
    }

    @Test
    public void updateArticle() throws Exception {
        // given
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId();

        UpdateArticleRequest request = new UpdateArticleRequest("변경 제목", "변경 내용");
        String updateJsonContent = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(put("/articles/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJsonContent));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(request.getTitle()));
    }

    @Test
    public void updateArticleException() throws Exception {
        // given
        Article article = repository.save(new Article("blog title", "blog content"));
        Long id = article.getId() + 1;

        UpdateArticleRequest request = new UpdateArticleRequest("변경 제목", "변경 내용");
        String updateJsonContent = objectMapper.writeValueAsString(request);

        // when
        ResultActions resultActions = mockMvc.perform(put("/articles/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonContent));

        // then
        resultActions.andExpect(status().isBadRequest());

        assertThrows(IllegalArgumentException.class, () -> blogService.update(id, request));
    }

}