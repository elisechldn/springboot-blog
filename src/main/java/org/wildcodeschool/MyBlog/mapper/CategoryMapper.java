package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.model.Image;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper {
    public CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if (category.getArticles() != null) {
            categoryDTO.setArticles(category.getArticles().stream().map(article -> {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setId(article.getId());
                articleDTO.setTitle(article.getTitle());
                articleDTO.setContent(article.getContent());
                articleDTO.setUpdatedAt(article.getUpdatedAt());
                articleDTO.setCategoryName(article.getCategory().getName());
                return articleDTO;
            }).collect(Collectors.toList()));
        }
        return categoryDTO;
    }

}
