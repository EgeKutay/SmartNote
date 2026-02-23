package com.SmartNote.demo.service;

import com.SmartNote.demo.model.Category;
import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.model.Note;
import com.SmartNote.demo.repository.CategoryRepository;
import com.SmartNote.demo.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;

    public NoteService(NoteRepository noteRepository, CategoryRepository categoryRepository) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
    }

    public Note create(Note note) {
        note.setCategory(resolveCategory(note.getContent()));
        return noteRepository.save(note);
    }

    public Note update(Note note) {
        note.setCategory(resolveCategory(note.getContent()));
        return noteRepository.save(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> findByUser(Long userId) {
        return noteRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public List<Note> findByUserAndCategory(Long userId, Long categoryId) {
        return noteRepository.findByUserIdAndCategoryIdAndIsDeletedFalse(userId, categoryId);
    }

    public void softDelete(Long id) {
        noteRepository.findById(id).ifPresent(note -> {
            note.setDeleted(true);
            noteRepository.save(note);
        });
    }

    // Scans note content against each category's keywords.
    // Returns the first matching Category, or UNCATEGORIZED as fallback.
    private Category resolveCategory(String content) {
        if (content == null || content.isBlank()) {
            return getUncategorized();
        }

        String lowerContent = content.toLowerCase();

        List<Category> allCategories = categoryRepository.findAll();

        for (Category category : allCategories) {
            if (category.getCategory() == CategoryType.UNCATEGORIZED) continue;
            if (category.getKeywords() == null || category.getKeywords().isBlank()) continue;

            String[] keywords = category.getKeywords().split(",");
            boolean matched = Arrays.stream(keywords)
                    .map(String::trim)
                    .anyMatch(keyword -> lowerContent.contains(keyword.toLowerCase()));
            if (matched) return category;
        }

        return getUncategorized();
    }

    private Category getUncategorized() {
        return categoryRepository.findByCategory(CategoryType.UNCATEGORIZED)
                .orElseThrow(() -> new IllegalStateException("UNCATEGORIZED category not seeded in database"));
    }
}
