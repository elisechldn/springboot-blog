package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
