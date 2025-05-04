package com.drawquest.services;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.dtos.ProgressUpdateDTO;
import com.drawquest.models.Progress;

import java.util.List;

public interface ProgressService {
    ProgressResponseDTO getProgressById(Long id);
    ProgressResponseDTO createProgress(ProgressCreateDTO progressCreateDTO);
    List<ProgressResponseDTO> getAllProgress();
    ProgressResponseDTO updateProgress(Long id, ProgressUpdateDTO progressUpdateDTO);
    void deleteProgress(Long id);
}
