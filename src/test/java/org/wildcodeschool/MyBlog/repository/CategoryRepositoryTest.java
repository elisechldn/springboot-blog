package org.wildcodeschool.MyBlog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.wildcodeschool.MyBlog.model.Category;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest //Load JPA/Hibernate only
public class CategoryRepositoryTest {

    @Autowired //Injection of the tested repo
    private CategoryRepository categoryRepository;

    @Test //Indicates the following is a unit test.
    void testFindAllCategories() {

        //ARANGE : création et sauvagarde de catégories en base
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        categoryRepository.saveAll(List.of(category1, category2));

        // ACT : récupération des categories via findAll()
        List<Category> categories = categoryRepository.findAll();

        //ASSERT : Vérifier la liste et si son contenu est correct
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("Category 1");
        assertThat(categories.get(1).getName()).isEqualTo("Category 2");
    }
}
