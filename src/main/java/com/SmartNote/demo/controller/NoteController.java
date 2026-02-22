package com.SmartNote.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SmartNote is running");
    }

    // TODO: add your endpoints here
}
