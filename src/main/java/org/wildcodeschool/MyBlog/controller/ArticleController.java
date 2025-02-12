package org.wildcodeschool.MyBlog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.service.ArticleService;


import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<ArticleDTO> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id) {
        ArticleDTO article = articleService.getArticleById(id);
        if (article == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(article);
    }

    @PostMapping
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article) {

        ArticleDTO savedArticle = articleService.createArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {
        ArticleDTO updatedArticle = articleService.updateArticle(id, articleDetails);
        if (updatedArticle == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedArticle);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        if (articleService.deleteArticle(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search-title")
    public ResponseEntity<List<ArticleDTO>>getArticlesByTitle(@RequestParam String searchTerms) {
        List<ArticleDTO> articles = articleService.getArticleByTitle(searchTerms);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/article-content")
    public ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String searchContent) {
        List<ArticleDTO> articles = articleService.getArticleByContent(searchContent);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/")
    public ResponseEntity<List<ArticleDTO>> getArticlesCreatedAfter(@RequestParam LocalDateTime searchDate) {
        List<ArticleDTO> articles = articleService.getArticlesCreatedAfter(searchDate);
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/last-articles")
    public ResponseEntity<List<ArticleDTO>>getFiveLastArticles() {
        List<ArticleDTO> articles = articleService.getFiveLastArticles();
        if (articles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(articles);
    }
 }
