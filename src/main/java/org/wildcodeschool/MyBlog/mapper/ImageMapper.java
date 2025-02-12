package org.wildcodeschool.MyBlog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.MyBlog.dto.ImageDTO;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Image;

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
}
