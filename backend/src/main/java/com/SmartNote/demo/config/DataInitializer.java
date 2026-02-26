package com.SmartNote.demo.config;

import com.SmartNote.demo.model.Category;
import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.repository.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    public DataInitializer(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) {
        Map<CategoryType, String> categoryKeywords = Map.of(
            CategoryType.TASK,          "improve,launch,do,meeting,client,project,deadline,apply,job,interview,clean,gather,form,create",
            CategoryType.SHOPPING,      "buy,price,order,shop",
            CategoryType.COOKING,       "recipe,pour,sizzle,cook",
            CategoryType.SPORT,         "exercise,gym,sets,workout,dumbbell,tennis,racket",
            CategoryType.HEALTH,        "neuron,brain,heart,hair,doctor,diet,run",
            CategoryType.UNCATEGORIZED, ""
        );

        categoryKeywords.forEach((type, keywords) -> {
            Category category = categoryRepository.findByCategory(type)
                    .orElse(new Category());
            category.setCategory(type);
            category.setKeywords(keywords);
            categoryRepository.save(category);
        });
    }
}
