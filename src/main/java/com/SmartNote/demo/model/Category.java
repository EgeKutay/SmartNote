package com.SmartNote.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private CategoryType category;

    private String keywords;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Note> notes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CategoryType getCategory() { return category; }
    public void setCategory(CategoryType category) { this.category = category; }
    public String getKeywords() { return keywords; }
    public void setKeywords(String keywords) { this.keywords = keywords; }
    public List<Note> getNotes() { return notes; }
    public void setNotes(List<Note> notes) { this.notes = notes; }
}
