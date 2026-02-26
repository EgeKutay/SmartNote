package com.SmartNote.demo.repository;

import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUserIdAndIsDeletedFalse(Long userId);
    List<Note> findByUserIdAndCategoryIdAndIsDeletedFalse(Long userId, Long categoryId);

    @Query("SELECT n FROM Note n WHERE n.user.id = :userId AND n.isDeleted = false " +
           "AND (CAST(:search AS string) IS NULL OR LOWER(n.content) LIKE LOWER(CONCAT('%', CAST(:search AS string), '%'))) " +
           "AND (:category IS NULL OR n.category.category = :category)")
    List<Note> findByUserWithFilters(@Param("userId") Long userId,
                                     @Param("search") String search,
                                     @Param("category") CategoryType category);
}
