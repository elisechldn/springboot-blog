package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.ArticleAuthor;

public interface ArticleAuthorRepository extends JpaRepository<ArticleAuthor, Long> {}
