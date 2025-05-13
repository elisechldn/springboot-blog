package org.wildcodeschool.MyBlog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.wildcodeschool.MyBlog.model.Article;
import org.wildcodeschool.MyBlog.model.Category;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ArticleRepositoryTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public static LocalDateTime generateDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        return LocalDateTime.parse(dateString, formatter);
    }

    @Test
    void testFindAllArticles() {
        //ARRANGE: Création de fausses données dans la base.
        Category category = new Category();
        category.setName("Tech");
        categoryRepository.save(category);

        Article article1 = new Article();
        article1.setTitle("Title 1");
        article1.setContent("Content 1");
        article1.setCategory(category);
        article1.setCreatedAt(generateDateTime("10-01-2024 13:13"));
        article1.setUpdatedAt(generateDateTime("10-01-2024 13:13"));


        Article article2 = new Article();
        article2.setTitle("Title 2");
        article2.setContent("Content 2");
        article2.setCategory(category);
        article2.setCreatedAt(generateDateTime("31-10-2024 12:12"));
        article2.setUpdatedAt(generateDateTime("31-10-2024 12:12"));

        articleRepository.saveAll(List.of(article1, article2));

        //ACT: Récupération de tous les articles
        List<Article> articles = articleRepository.findAll();

        //ASSERT
        assertThat(articles).hasSize(2);
        assertThat(articles.get(0)).isEqualTo(article1);
        assertThat(articles.get(1)).isEqualTo(article2);
    }

    @Test
    void testFindByTitle() {
        //ARRANGE: Création de fausses données dans la base.
        Article article = new Article();
        article.setTitle("Unique title");
        article.setContent("Some content");
        article.setCreatedAt(generateDateTime("06-01-2024 13:13"));
        article.setUpdatedAt(generateDateTime("06-01-2024 13:13"));

        articleRepository.save(article);

        //ACT: Récupération de tous les articles
        List<Article> foundArticle = articleRepository.findByTitle("Unique title");

        //ASSERT
        assertThat(foundArticle).hasSize(1);
        assertThat(foundArticle.get(0).getTitle()).isEqualTo("Unique title");
        assertThat(foundArticle.get(0).getContent()).isEqualTo("Some content");
    }

    @Test
    void testFindByContentContaining() {
        //ARRANGE
        Article article = new Article();
        article.setTitle("Some title");
        article.setContent("Specific content for testing");
        article.setCreatedAt(generateDateTime("06-01-2024 14:14"));
        article.setUpdatedAt(generateDateTime("06-01-2024 14:14"));

        articleRepository.save(article);

        //ACT
        List<Article> articleByContent = articleRepository.findByContentContaining("Specific content for testing");

        //ASSERT
        assertThat(articleByContent).hasSize(1);
        assertThat(articleByContent.get(0).getContent()).contains("Specific");
    }

    @Test
    void testFindByCreatedAtAfter() {
        //ARRANGE
                Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setContent("Content 1");
        article1.setCreatedAt(generateDateTime("01-01-2024 15:15"));
        article1.setUpdatedAt(generateDateTime("01-01-2024 15:30"));


        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setContent("Content 2");
        article2.setCreatedAt(generateDateTime("31-10-2024 16:16"));
        article2.setUpdatedAt(generateDateTime("31-10-2024 16:16"));

        articleRepository.saveAll(List.of(article1, article2));

        //ACT
        List<Article> createdAfterArticle = articleRepository.findByCreatedAtAfter(generateDateTime("02-01-2024 15:15"));

        //ASSERT
        assertThat(createdAfterArticle).hasSize(1);
        assertThat(createdAfterArticle.get(0).getTitle()).isEqualTo("Article 2");
    }

    @Test
    void testFindTop5ByOrderByCreatedAtDesc() {
        //ARRANGE
        Article article1 = new Article();
        article1.setTitle("Article 1");
        article1.setContent("Content 1");
        article1.setCreatedAt(generateDateTime("01-01-2024 15:15"));
        article1.setUpdatedAt(generateDateTime("01-01-2024 15:30"));


        Article article2 = new Article();
        article2.setTitle("Article 2");
        article2.setContent("Content 2");
        article2.setCreatedAt(generateDateTime("02-01-2024 16:16"));
        article2.setUpdatedAt(generateDateTime("02-01-2024 16:16"));

        Article article3 = new Article();
        article3.setTitle("Article 3");
        article3.setContent("Content 3");
        article3.setCreatedAt(generateDateTime("03-01-2024 16:16"));
        article3.setUpdatedAt(generateDateTime("03-01-2024 16:16"));

        Article article4 = new Article();
        article4.setTitle("Article 4");
        article4.setContent("Content 4");
        article4.setCreatedAt(generateDateTime("04-01-2024 17:17"));
        article4.setUpdatedAt(generateDateTime("04-01-2024 17:30"));

        Article article5 = new Article();
        article5.setTitle("Article 5");
        article5.setContent("Content 5");
        article5.setCreatedAt(generateDateTime("05-01-2024 17:17"));
        article5.setUpdatedAt(generateDateTime("05-01-2024 17:17"));

        Article article6 = new Article();
        article6.setTitle("Article 6");
        article6.setContent("Content 6");
        article6.setCreatedAt(generateDateTime("06-01-2024 17:17"));
        article6.setUpdatedAt(generateDateTime("06-01-2024 17:17"));

        articleRepository.saveAll(List.of(article1, article2, article3, article4, article5, article6));
        //ACT
        List<Article> topFiveArticles = articleRepository.findTop5ByOrderByCreatedAtDesc();

        //ASSERT
        assertThat(topFiveArticles).hasSize(5);
        assertThat(topFiveArticles.get(0)).isEqualTo(article6);
        assertThat(topFiveArticles.get(1)).isEqualTo(article5);
        assertThat(topFiveArticles.get(2)).isEqualTo(article4);
        assertThat(topFiveArticles.get(3)).isEqualTo(article3);
        assertThat(topFiveArticles.get(4)).isEqualTo(article2);
    }
}
