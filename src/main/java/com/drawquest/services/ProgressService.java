package com.drawquest.services;

import com.drawquest.dtos.ProgressResponseDTO;

import java.util.List;

public interface ProgressService {
    ProgressResponseDTO getProgressById(Long id, String username);
    List<ProgressResponseDTO> getAllProgress(String username);
}
