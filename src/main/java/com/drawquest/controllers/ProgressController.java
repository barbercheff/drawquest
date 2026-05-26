package com.drawquest.controllers;

import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.services.ProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/progress")
public class ProgressController {

    private final ProgressService progressService;

    public ProgressController(ProgressService progressService) {
        this.progressService = progressService;
    }

    @GetMapping
    public ResponseEntity<List<ProgressResponseDTO>> getAllProgress(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.getAllProgress(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgressResponseDTO> getProgressById(@PathVariable Long id,
                                                               @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(progressService.getProgressById(id, userDetails.getUsername()));
    }
}
