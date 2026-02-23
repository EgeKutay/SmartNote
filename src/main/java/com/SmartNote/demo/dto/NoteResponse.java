package com.SmartNote.demo.dto;

import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.model.Note;

import java.time.LocalDateTime;

public class NoteResponse {
    private Long id;
    private String content;
    private CategoryType category;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public NoteResponse(Note note) {
        this.id = note.getId();
        this.content = note.getContent();
        this.category = note.getCategory() != null ? note.getCategory().getCategory() : null;
        this.createdDate = note.getCreatedDate();
        this.modifiedDate = note.getModifiedDate();
    }

    public Long getId() { return id; }
    public String getContent() { return content; }
    public CategoryType getCategory() { return category; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public LocalDateTime getModifiedDate() { return modifiedDate; }
}
