package org.wildcodeschool.MyBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wildcodeschool.MyBlog.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
