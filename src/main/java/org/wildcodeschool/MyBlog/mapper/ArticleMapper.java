package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.dto.AuthorDTO;
import org.wildcodeschool.MyBlog.dto.CreateArticleDTO;
import org.wildcodeschool.MyBlog.model.*;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {
    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setCreatedAt(article.getCreatedAt());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }

        if (article.getArticleAuthors() != null) {
            articleDTO.setAuthors(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        AuthorDTO authorDTO = new AuthorDTO();
                        authorDTO.setId(articleAuthor.getAuthor().getId());
                        authorDTO.setFirstname(articleAuthor.getAuthor().getFirstname());
                        authorDTO.setLastname(articleAuthor.getAuthor().getLastname());
                        return authorDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return articleDTO;
    }

    public Article convertToEntity(CreateArticleDTO createArticleDTO) {
        Article article = new Article();
        article.setTitle(createArticleDTO.getTitle());
        article.setContent(createArticleDTO.getContent());
        if (createArticleDTO.getCategoryId() != null) {
            article.setCategory(new Category());
        }
        if (createArticleDTO.getImages() != null) {
            //Transformation de la liste String en liste d'objets Image. Le map tranforme url qui fait référence à imageUrl dans le DTO en image
            article.setImages(createArticleDTO.getImages().stream()
                    .map(imageDTO -> new Image())
                    .collect(Collectors.toList()));
        }

        if (createArticleDTO.getAuthors() != null) {
            article.setArticleAuthors(createArticleDTO.getAuthors().stream()
                    .map(authorContributionDTO -> new ArticleAuthor()
                            ).collect(Collectors.toList()));
        }
        return article;
    }
}
