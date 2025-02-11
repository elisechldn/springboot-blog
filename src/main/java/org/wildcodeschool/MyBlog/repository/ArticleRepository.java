package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Article;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    //Recherche par titre
    List<Article> findByTitle(String title);

    //Recherche selon une chaîne de caractère
    List<Article> findByContentContaining(String content);

    //Recherche selon une date
    List<Article> findByCreatedAtAfter(LocalDateTime date);

    //Recherche 5 derniers articles du plus récent au plus ancien
    List<Article> findFiveLastArticlesOrderByCreatedAtDesc();
}
