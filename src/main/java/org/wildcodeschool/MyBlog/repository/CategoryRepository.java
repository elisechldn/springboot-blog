package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
