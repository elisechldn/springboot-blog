package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
