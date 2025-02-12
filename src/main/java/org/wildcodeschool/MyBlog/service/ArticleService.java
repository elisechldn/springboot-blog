package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.ArticleDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.ArticleMapper;
import org.wildcodeschool.MyBlog.model.*;
import org.wildcodeschool.MyBlog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorRepository authorRepository;
    private final ArticleMapper articleMapper;

    public ArticleService(ArticleRepository articleRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ArticleAuthorRepository articleAuthorRepository, AuthorRepository authorRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
        this.articleMapper = articleMapper;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article trouvé.");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(@PathVariable Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("l'article avec l'id " + id +" n'a pas été trouvé."));
        return articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(@RequestBody Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        if(article.getCategory() !=null) {
            Category category = categoryRepository.findById(article.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("La catégorie que vous avez sélectionné est introuvable."));
            article.setCategory(category);
        }

        if(article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null;
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
            }

        Article savedArticle = articleRepository.save(article);

        if (article.getArticleAuthors() != null) {
            //Parcours de la liste d'articleAuthor associé à l'article
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {

                //Récupération de l'auteur associé
                Author author = articleAuthor.getAuthor();

                //Recherche dans la BDD s'il est existant ou non. Si non: erreur.
                author = authorRepository.findById(author.getId()).orElse(null);
                if (author == null) {
                    return null;
                }
                //Si existant, enregistrement de l'auteur, de l'article enregistré et de la contribution
                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());

                //Mise à jour de l'articleAuthor.
                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(@PathVariable Long id, @RequestBody Article articleDetails) {

        Article article = articleRepository.findById(id).orElse(null);
        if (article == null) {
            return null;
        }
        article.setTitle(articleDetails.getTitle());
        article.setContent(articleDetails.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        if(articleDetails.getCategory() !=null) {
            Category category = categoryRepository.findById(articleDetails.getCategory().getId()).orElse(null);
            if (category == null) {
                return null;
            }
            article.setCategory(category);
        }

        if (articleDetails.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : articleDetails.getImages()) {
                if (image.getId() != null) {
                    // Vérification des images existantes
                    Image existingImage = imageRepository.findById(image.getId()).orElse(null);
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    } else {
                        return null; // Image non trouvée, retour d'une erreur
                    }
                } else {
                    // Création de nouvelles images
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            // Mettre à jour la liste des images associées
            article.setImages(validImages);
        } else {
            // Si aucune image n'est fournie, on nettoie la liste des images associées
            article.getImages().clear();
        }

        if (articleDetails.getArticleAuthors() != null) {
            /* Si des auteurs sont associés aux articles de articleDetails,
             * on les parcourt pour les supprimer */
            for (ArticleAuthor oldArticleAuthor : articleDetails.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            //Déclaration d'une nouvelle liste pour stocker les auteurs de l'article mis à jour
            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            //Parcours la liste des auteurs associés dans articleDetails
            for (ArticleAuthor articleAuthorDetails : articleDetails.getArticleAuthors()) {

                //Récupération de l'auteur associé à l'articleAuthor
                Author author = articleAuthorDetails.getAuthor();

                //Recherche par l'ID dans la BDD
                author = authorRepository.findById(author.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("L'auteur que vous avez sélectionné est introuvable."));


                /**Création d'un nouvel ArticleAuthor qui sera ajouté aux auteurs mis à jour
                 * Affectation de l'auteur, de l'article et de sa contribution.
                 * /!\ Enregistrement dans la liste
                 */
                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);

                //Sauvegarge de tous les articleAuthor dans la BDD.
                for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                    articleAuthorRepository.save(articleAuthor);
                }

                //Mise à jour de l'article
                article.setArticleAuthors(updatedArticleAuthors);
            }
        }
        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public boolean deleteArticle(@PathVariable Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("L'article que vous souhaitez supprimé est introuvable."));
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
        return true;
    }

    public List<ArticleDTO>getArticleByTitle(@RequestParam String searchTerms) {
        List<Article> articles = articleRepository.findByTitle(searchTerms);
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article ne correspond au titre recherché.");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticleByContent(@RequestParam String searchContent) {
        List<Article> articles = articleRepository.findByContentContaining(searchContent);
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article ne correspond à ce contenu.");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO> getArticlesCreatedAfter(@RequestParam LocalDateTime searchDate) {
        List<Article> articles = articleRepository.findByCreatedAtAfter(searchDate);
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article postérieur à cette date n'a été trouvé.");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public List<ArticleDTO>getFiveLastArticles() {
        List<Article> articles = articleRepository.findFiveLastArticlesOrderByCreatedAtDesc();
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("Aucun article correspondant à votre recherche n'a été trouvé.");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }
}
