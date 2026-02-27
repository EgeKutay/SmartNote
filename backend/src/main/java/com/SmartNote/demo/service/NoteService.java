package com.SmartNote.demo.service;

import com.SmartNote.demo.model.Category;
import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.model.Note;
import com.SmartNote.demo.repository.CategoryRepository;
import com.SmartNote.demo.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final CategoryRepository categoryRepository;
    private final GeminiService geminiService;

    public NoteService(NoteRepository noteRepository, CategoryRepository categoryRepository, GeminiService geminiService) {
        this.noteRepository = noteRepository;
        this.categoryRepository = categoryRepository;
        this.geminiService = geminiService;
    }

    public Note create(Note note) {
        enrichWithAI(note);
        return noteRepository.save(note);
    }

    public Note update(Note note) {
        enrichWithAI(note);
        return noteRepository.save(note);
    }

    public Optional<Note> findById(Long id) {
        return noteRepository.findById(id);
    }

    public List<Note> findByUser(Long userId) {
        return noteRepository.findByUserIdAndIsDeletedFalse(userId);
    }

    public List<Note> findFiltered(Long userId, String search, CategoryType category) {
        if (search == null || search.isBlank()) {
            return noteRepository.findByUserWithCategory(userId, category);
        }
        return noteRepository.findByUserWithSearchAndCategory(userId, search.trim(), category);
    }

    public void softDelete(Long id) {
        noteRepository.findById(id).ifPresent(note -> {
            note.setDeleted(true);
            noteRepository.save(note);
        });
    }

    // Uses Gemini AI to generate a title (if blank) and classify the category.
    private void enrichWithAI(Note note) {
        if (note.getTitle() == null || note.getTitle().isBlank()) {
            note.setTitle(geminiService.generateTitle(note.getContent()));
        }

        CategoryType categoryType = geminiService.classifyCategory(note.getTitle(), note.getContent());
        Category category = categoryRepository.findByCategory(categoryType)
                .orElseGet(this::getUncategorized);
        note.setCategory(category);
    }

    private Category getUncategorized() {
        return categoryRepository.findByCategory(CategoryType.UNCATEGORIZED)
                .orElseThrow(() -> new IllegalStateException("UNCATEGORIZED category not seeded in database"));
    }
}
