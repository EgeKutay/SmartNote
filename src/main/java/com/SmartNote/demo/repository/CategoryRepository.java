package com.SmartNote.demo.repository;

import com.SmartNote.demo.model.Category;
import com.SmartNote.demo.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategory(CategoryType category);
    List<Category> findAll();
}
