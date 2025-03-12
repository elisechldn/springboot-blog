package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Image;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageMapper {
    public ImageDTO convertToDTO(Image image) {
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setId(image.getId());
        imageDTO.setUrl(image.getUrl());
        if (image.getArticles() != null) {
            imageDTO.setArticleIds(image.getArticles().stream().map(Article::getId).collect(Collectors.toList()));
        }
        return imageDTO;
    }

    public Image convertToEntity(ImageDTO imageDTO) {
        Image image = new Image();
        image.setId(imageDTO.getId());
        image.setUrl((imageDTO.getUrl()));
        //Many to Many sans attribut
        if (imageDTO.getArticleIds() != null) {
            List<Article> articles = imageDTO.getArticleIds().stream()
                    .map(id -> {
                        Article article = new Article();
                        article.setId(id);
                        return article;
                    }).collect(Collectors.toList());
        }
        return image;
    }
}
