package com.drawquest.services;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.dtos.ProgressUpdateDTO;

import java.util.List;

public interface ProgressService {
    ProgressResponseDTO getProgressById(Long id, String username);
    ProgressResponseDTO createProgress(ProgressCreateDTO progressCreateDTO, String username);
    List<ProgressResponseDTO> getAllProgress(String username);
    ProgressResponseDTO updateProgress(Long id, ProgressUpdateDTO progressUpdateDTO, String username);
    void deleteProgress(Long id, String username);
}
