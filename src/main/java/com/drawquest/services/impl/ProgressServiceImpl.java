package com.drawquest.services.impl;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.dtos.ProgressUpdateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.ProgressMapper;
import com.drawquest.models.Progress;
import com.drawquest.models.Quest;
import com.drawquest.models.User;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.ProgressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgressServiceImpl implements ProgressService {

    private final ProgressRepository progressRepository;


    private final UserRepository userRepository;


    private final QuestRepository questRepository;

    public ProgressServiceImpl(ProgressRepository progressRepository, UserRepository userRepository, QuestRepository questRepository) {
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
    }

    @Override
    public ProgressResponseDTO getProgressById(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso con ID " + id + " no encontrado"));
        return ProgressMapper.toProgressResponseDTO(progress);
    }

    @Override
    public ProgressResponseDTO createProgress(ProgressCreateDTO progressCreateDTO) {
        User user = userRepository.findById(progressCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario con ID " + progressCreateDTO.getUserId() + " no encontrado"));
        Quest quest = questRepository.findById(progressCreateDTO.getQuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Quest con ID " + progressCreateDTO.getQuestId() + " no encontrada"));

        Progress progress = ProgressMapper.toProgressEntity(progressCreateDTO, user, quest);

        return ProgressMapper.toProgressResponseDTO(progressRepository.save(progress));
    }

    @Override
    public List<ProgressResponseDTO> getAllProgress() {
        return progressRepository.findAll().stream()
                .map(ProgressMapper::toProgressResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProgressResponseDTO updateProgress(Long id, ProgressUpdateDTO progressUpdateDTO) {
        Progress existingProgress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso con ID " + id + " no encontrado"));


        existingProgress.setAttempts(progressUpdateDTO.getAttempts());
        existingProgress.setCompleted(progressUpdateDTO.getCompleted());

        return ProgressMapper.toProgressResponseDTO(progressRepository.save(existingProgress));
    }

    @Override
    public void deleteProgress(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progreso con ID " + id + " no encontrado"));
        progressRepository.delete(progress);
    }
}

