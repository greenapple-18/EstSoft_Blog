package com.estsoft.springproject.blog.controller;

import com.estsoft.springproject.blog.service.BlogService;
import com.estsoft.springproject.blog.service.CommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ExternalApiControllerTest {

    @Mock
    private BlogService blogService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private ExternalApiController externalApiController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(externalApiController).build();
    }

    @Test
    void testCallArticlesApi() throws Exception {
        // given
        when(blogService.saveArticle(any())).thenReturn(null);

        // when
        mockMvc.perform(get("/api/external/articles"))
                .andExpect(status().isOk())
                .andExpect(content().string("OK"))
                .andReturn();
    }

    @Test
    void testCallCommentsApi() throws Exception {
        // given
        when(commentService.saveComment(anyLong(), any())).thenReturn(null);

        // when
        mockMvc.perform(get("/api/external/comments"))
                .andExpect(status().isOk()) // Expect HTTP 201 Created
                .andExpect(content().string("OK"))
                .andReturn();
    }
}
