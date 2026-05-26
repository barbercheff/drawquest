package com.drawquest.services.impl;

import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.ProgressMapper;
import com.drawquest.models.Progress;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.services.ProgressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    @Override
    public ProgressResponseDTO getProgressById(Long id, String username) {
        Progress progress = progressRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + id + " not found"));
        return ProgressMapper.toProgressResponseDTO(progress);
    }

    @Override
    public List<ProgressResponseDTO> getAllProgress(String username) {
        return progressRepository.findByUserUsername(username).stream()
                .map(ProgressMapper::toProgressResponseDTO)
                .collect(Collectors.toList());
    }
}
