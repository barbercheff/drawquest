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
                .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + id + " not found"));
        return ProgressMapper.toProgressResponseDTO(progress);
    }

    @Override
    public ProgressResponseDTO createProgress(ProgressCreateDTO progressCreateDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));
        Quest quest = questRepository.findById(progressCreateDTO.getQuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + progressCreateDTO.getQuestId() + " not found"));

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
                .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + id + " not found"));

        existingProgress.setAttempts(progressUpdateDTO.getAttempts());
        existingProgress.setCompleted(progressUpdateDTO.getCompleted());

        return ProgressMapper.toProgressResponseDTO(progressRepository.save(existingProgress));
    }

    @Override
    public void deleteProgress(Long id) {
        Progress progress = progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + id + " not found"));
        progressRepository.delete(progress);
    }
}
