package com.SmartNote.demo.controller;

import com.SmartNote.demo.dto.NoteRequest;
import com.SmartNote.demo.dto.NoteResponse;
import com.SmartNote.demo.model.CategoryType;
import com.SmartNote.demo.model.Note;
import com.SmartNote.demo.model.User;
import com.SmartNote.demo.service.NoteService;
import com.SmartNote.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SmartNote is running");
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody NoteRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setContent(request.getContent());
        note.setUser(user);

        Note saved = noteService.create(note);
        return ResponseEntity.ok(new NoteResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<NoteResponse>> getAll(@AuthenticationPrincipal UserDetails userDetails,
                                                     @RequestParam(required = false) String search,
                                                     @RequestParam(required = false) CategoryType category) {
        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<NoteResponse> notes = noteService.findFiltered(user.getId(), search, category)
                .stream().map(NoteResponse::new).toList();

        return ResponseEntity.ok(notes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody NoteRequest request,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Note note = noteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        note.setContent(request.getContent());
        Note saved = noteService.update(note);
        return ResponseEntity.ok(new NoteResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Note note = noteService.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        User user = userService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!note.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied.");
        }

        noteService.softDelete(id);
        return ResponseEntity.ok("Note deleted.");
    }
}
