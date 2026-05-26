package com.drawquest.controllers;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.dtos.ProgressUpdateDTO;
import com.drawquest.services.ProgressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<List<ProgressResponseDTO>> getAllProgress(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.getAllProgress(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressResponseDTO> getProgressById(@PathVariable Long id,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.getProgressById(id, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<ProgressResponseDTO> createProgress(@Valid @RequestBody ProgressCreateDTO progressCreateDTO,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(progressService.createProgress(progressCreateDTO, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProgressResponseDTO> updateProgress(@PathVariable Long id,
                                                              @RequestBody ProgressUpdateDTO progressUpdateDTO,
                                                              @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.updateProgress(id, progressUpdateDTO, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgress(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        progressService.deleteProgress(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
