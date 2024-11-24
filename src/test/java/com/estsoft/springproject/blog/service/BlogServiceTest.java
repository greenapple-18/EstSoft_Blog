package com.estsoft.springproject.blog.service;

import com.estsoft.springproject.blog.domain.Article;
import com.estsoft.springproject.blog.domain.dto.AddArticleRequest;
import com.estsoft.springproject.blog.domain.dto.UpdateArticleRequest;
import com.estsoft.springproject.blog.repository.BlogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @InjectMocks
    private BlogService blogService;

    @Mock
    private BlogRepository blogRepository;

    @Test
    void testSaveArticle() {
        // given
        AddArticleRequest request = new AddArticleRequest("title", "content");
        Article savedArticle = new Article("title", "content");

        // stub
        when(blogRepository.save(any(Article.class))).thenReturn(savedArticle);

        // when
        Article result = blogService.saveArticle(request);

        // then
        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals("content", result.getContent());
        verify(blogRepository, times(1)).save(any(Article.class));
    }

    @Test
    void testFindAll() {
        // given
        List<Article> articles = Arrays.asList(
                new Article("title1", "content1"),
                new Article("title2", "content2")
        );

        // stub
        when(blogRepository.findAll()).thenReturn(articles);

        // when
        List<Article> result = blogService.findAll();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(blogRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Success() {
        // given
        Long id = 1L;
        Article article = new Article("title", "content");

        // stub
        when(blogRepository.findById(id)).thenReturn(Optional.of(article));

        // when
        Article result = blogService.findById(id);

        // then
        assertNotNull(result);
        assertEquals("title", result.getTitle());
        assertEquals("content", result.getContent());
        verify(blogRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_NotFound() {
        // given
        Long id = 1L;

        // stub
        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        // when & then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            blogService.findById(id);
        });

        assertEquals("not found id: " + id, exception.getMessage());
        verify(blogRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteById() {
        // given
        Long id = 1L;

        // when
        blogService.deleteById(id);

        // then
        verify(blogRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdate() {
        // given
        Long id = 1L;
        Article existingArticle = new Article("oldTitle", "oldContent");
        UpdateArticleRequest request = new UpdateArticleRequest("newTitle", "newContent");

        // stub
        when(blogRepository.findById(id)).thenReturn(Optional.of(existingArticle));

        // when
        Article updatedArticle = blogService.update(id, request);

        // then
        assertNotNull(updatedArticle);
        assertEquals("newTitle", updatedArticle.getTitle());
        assertEquals("newContent", updatedArticle.getContent());
        verify(blogRepository, times(1)).findById(id);
    }
}
