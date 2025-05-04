package com.drawquest.controllers;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.dtos.ProgressUpdateDTO;
import com.drawquest.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<ProgressResponseDTO>> getAllProgress() {
        return ResponseEntity.ok(progressService.getAllProgress());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressResponseDTO> getProgressById(@PathVariable Long id) {
        return ResponseEntity.ok(progressService.getProgressById(id));
    }

    @PostMapping
    public ResponseEntity<ProgressResponseDTO> createProgress(@RequestBody ProgressCreateDTO progressCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(progressService.createProgress(progressCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgressResponseDTO> updateProgress(@PathVariable Long id, @RequestBody ProgressUpdateDTO progressUpdateDTO) {
        return ResponseEntity.ok(progressService.updateProgress(id, progressUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id) {
        progressService.deleteProgress(id);
        return ResponseEntity.noContent().build();
    }
}
