package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BlogControllerTest {
    @InjectMocks
    BlogController blogController;

    @Mock
    BlogService blogService;

    MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(blogController).build();
    }

    // 블로그 게시글 정보 저장 API 테스트
    @Test
    void testSaveArticle() throws Exception {
        // given : API 호출에 필요한 데이터 생성, 직렬화 (객체 -> json형식)
        String title = "mock_title";
        String content = "mock_content";

        AddArticleRequest request = new AddArticleRequest(title, content);
        ObjectMapper objectMapper = new ObjectMapper();
        String articleJson = objectMapper.writeValueAsString(request);

        // stub (service.saveArticle 호출 시 위에 만든 article을 리턴하도록 처리)
        Mockito.when(blogService.saveArticle(any()))
                .thenReturn(new Article(title, content));

        // when : API 호출
        ResultActions resultActions = mockMvc.perform(post("/api/articles")
                .content(articleJson)
                .contentType(MediaType.APPLICATION_JSON));

        // then : 호출 결과 검증
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("title").value(title))
                .andExpect(jsonPath("content").value(content));
    }

    @Test
    public void testDeleteArticle() throws Exception {
        // given
        Long id = 1L;

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/articles/{id}", id));

        // then
        resultActions.andExpect(status().isOk());
        Mockito.verify(blogService, times(1)).deleteById(id);
    }

    @Test
    void testFindOne() throws Exception {
        // given
        Long id = 1L;
        String title = "mock_title";
        String content = "mock_content";
        Article article = new Article(title, content);
        article.setId(1L);

        Mockito.when(blogService.findById(any()))
                .thenReturn(article);

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/{id}", id));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(article.getId()))
                .andExpect(jsonPath("title").value(article.getTitle()))
                .andExpect(jsonPath("content").value(article.getContent()));

    }

    @Test
    void testFindAllArticles() throws Exception {
        // given
        Article article1 = new Article("title1", "content1");
        Article article2 = new Article("title2", "content2");

        Mockito.when(blogService.findAll())
                .thenReturn(List.of(article1, article2));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles")
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value(article1.getTitle()))
                .andExpect(jsonPath("$[0].content").value(article1.getContent()))
                .andExpect(jsonPath("$[1].title").value(article2.getTitle()))
                .andExpect(jsonPath("$[1].content").value(article2.getContent()));

        Mockito.verify(blogService, times(1)).findAll();
    }

    @Test
    void testUpdateArticleById() throws Exception {
        // given
        Long id = 1L;
        String updatedTitle = "Updated Title";
        String updatedContent = "Updated Content";

        UpdateArticleRequest request = new UpdateArticleRequest(updatedTitle, updatedContent);
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(request);

        Article updatedArticle = new Article(updatedTitle, updatedContent);
        updatedArticle.setId(id);

        Mockito.when(blogService.update(Mockito.eq(id), Mockito.any(UpdateArticleRequest.class)))
                .thenReturn(updatedArticle);

        // when
        ResultActions resultActions = mockMvc.perform(put("/api/articles/{id}", id)
                .content(requestJson)
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("id").value(id))
                .andExpect(jsonPath("title").value(updatedTitle))
                .andExpect(jsonPath("content").value(updatedContent));

        // 요청 객체 확인
        Mockito.verify(blogService, times(1)).update(Mockito.eq(id), Mockito.any(UpdateArticleRequest.class));
    }


    @Test
    void testHandleIllegalArgumentException() throws Exception {
        // given
        Long id = 1L;
        String errorMessage = "Invalid article ID";

        Mockito.when(blogService.findById(id))
                .thenThrow(new IllegalArgumentException(errorMessage));

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/articles/{id}", id));

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(errorMessage));

        Mockito.verify(blogService, times(1)).findById(id);
    }
}