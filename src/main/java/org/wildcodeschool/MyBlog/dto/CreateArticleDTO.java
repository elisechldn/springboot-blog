package org.wildcodeschool.MyBlog.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public class CreateArticleDTO {

    @NotBlank(message= "Le titre ne peut pas être vide.")
    @Size(min=2, max= 50, message= "Le titre doit contenir entre 2 et 50 caractères.")
    private String title;

    @NotBlank(message= "Le corps ne peut pas être vide.")
    @Size(min= 45, message= "Le contenu doit être de 45 caractères minimum.")
    private String content;

    @NotBlank(message= "Veuillez attribuer une catégorie à l'article.")
    @Positive(message= "L'ID de la catégorie doit être positif.")
    private Long categoryId;

    @NotEmpty(message= "Votre article doit contenir au moins une image.")
    private List<@Valid ImageDTO> images;

    @NotEmpty(message= "La liste d'auteur ne doit pas être vide")
    private List<@Valid AuthorContributionDTO> authors;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategory(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<ImageDTO> getImages() {
        return images;
    }

    public void setImages(List<ImageDTO> images) {
        this.images = images;
    }

    public List<AuthorContributionDTO> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorContributionDTO> authors) {
        this.authors = authors;
    }
}
