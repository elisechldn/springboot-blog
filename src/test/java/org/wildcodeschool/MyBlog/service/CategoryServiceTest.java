package org.wildcodeschool.MyBlog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
import org.wildcodeschool.MyBlog.exception.ResourceNotFoundException;
import org.wildcodeschool.MyBlog.mapper.CategoryMapper;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category1 = new Category();
        category1.setName("Category 1");

        Category category2 = new Category();
        category2.setName("Category 2");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        CategoryDTO categoryDto1 = new CategoryDTO();
        categoryDto1.setName("Category 1");

        CategoryDTO categoryDto2 = new CategoryDTO();
        categoryDto2.setName("Category 2");

        //Simulation du comportement du Mapper
        when(categoryMapper.convertToDTO(category1)).thenReturn(categoryDto1);
        when(categoryMapper.convertToDTO(category2)).thenReturn(categoryDto2);

        // Act
        List<CategoryDTO> categories = categoryService.getAllCategories();

        // Assert
        assertThat(categories).hasSize(2);
        assertThat(categories.get(0).getName()).isEqualTo("Category 1");
        assertThat(categories.get(1).getName()).isEqualTo("Category 2");
    }

    @Test
    void testGetCategoryById_CategoryExists() {
        //Arrange
        Category category = new Category();
        category.setName("Category 1");

        when(categoryRepository.findById(7)).thenReturn(Optional.of(category));

        CategoryDTO categoryDto = new CategoryDTO();
        categoryDto.setName("Category 1");

        when(categoryMapper.convertToDTO(category)).thenReturn(categoryDto);

        //Act
        CategoryDTO result = categoryService.getCategoryById(7);

        //Assert
        assertThat(result.getName()).isEqualTo("Category 1");
    }

    @Test
    void testGetCategoryById_CategoryNotFound() {
        //Arrange
        when(categoryRepository.findById(7)).thenReturn(Optional.empty());

        //Act and Assert
        assertThatThrownBy(() -> categoryService.getCategoryById(7))
                .isInstanceOf((ResourceNotFoundException.class))
                .hasMessage("La catégorie correspondant à l'id 7 n'a pas été trouvé.");
    }
}
