package com.SmartNote.demo.service;

import com.SmartNote.demo.model.Category;
import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> findByType(CategoryType type) {
        return categoryRepository.findByCategory(type);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
