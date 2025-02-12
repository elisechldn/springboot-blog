package org.wildcodeschool.MyBlog.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.MyBlog.dto.CategoryDTO;
import org.wildcodeschool.MyBlog.mapper.CategoryMapper;
import org.wildcodeschool.MyBlog.model.Category;
import org.wildcodeschool.MyBlog.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }


    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            return null;
        }
        return categories.stream().map(categoryMapper::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(@PathVariable Integer id) {
        Category category = categoryRepository.findById (id).orElse(null);
        if (category == null) {
            return null;
        }
        return categoryMapper.convertToDTO(category);
    }

    public CategoryDTO createCategory(@RequestBody Category category) {
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(savedCategory);
    }

    public CategoryDTO updateCategory(@PathVariable Integer id, @RequestBody Category categoryDetails) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return null;
        }
        category.setName(categoryDetails.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(updatedCategory);
    }

    public boolean deleteCategory(@PathVariable Integer id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return false;
        }
        categoryRepository.delete(category);
        return true;
    }
}
