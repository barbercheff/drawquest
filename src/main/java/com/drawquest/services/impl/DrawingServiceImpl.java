package com.drawquest.services.impl;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.DrawingMapper;
import com.drawquest.models.Drawing;
import com.drawquest.models.Progress;
import com.drawquest.models.Quest;
import com.drawquest.models.User;
import com.drawquest.repositories.DrawingRepository;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.DrawingService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrawingServiceImpl implements DrawingService {

    private final DrawingRepository drawingRepository;

    private final UserRepository userRepository;

    private final QuestRepository questRepository;

    private final ProgressRepository progressRepository;

    public DrawingServiceImpl(DrawingRepository drawingRepository, UserRepository userRepository, QuestRepository questRepository,
                              ProgressRepository progressRepository) {
        this.drawingRepository = drawingRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.progressRepository = progressRepository;
    }

    @Override
    public DrawingResponseDTO getDrawingById(Long id, String username) {
        Drawing drawing = drawingRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
        return DrawingMapper.toDrawingResponseDTO(drawing);
    }

    @Override
    @Transactional
    public DrawingResponseDTO createDrawing(DrawingCreateDTO drawingCreateDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + " not found"));

        Quest quest = questRepository.findById(drawingCreateDTO.getQuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + drawingCreateDTO.getQuestId() + " not found"));

        Progress progress = progressRepository.findByUserIdAndQuestId(user.getId(), quest.getId())
                .orElseGet(() -> createProgress(user, quest));
        progress.setAttempts(progress.getAttempts() + 1);
        progressRepository.save(progress);

        Drawing drawing = DrawingMapper.toDrawingEntity(drawingCreateDTO, user, quest);
        return DrawingMapper.toDrawingResponseDTO(drawingRepository.save(drawing));
    }

    @Override
    public List<DrawingResponseDTO> getAllDrawings(String username) {
        List<Drawing> drawings = drawingRepository.findByUserUsername(username);
        return drawings.stream()
                .map(DrawingMapper::toDrawingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DrawingResponseDTO updateDrawing(Long id, DrawingUpdateDTO drawingUpdateDTO, String username) {
        Drawing existingDrawing = drawingRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));

        if (drawingUpdateDTO.getImageUrl() != null) {
            existingDrawing.setImageUrl(drawingUpdateDTO.getImageUrl());
        }

        existingDrawing.setModifiedAt(LocalDateTime.now());

        return DrawingMapper.toDrawingResponseDTO(drawingRepository.save(existingDrawing));
    }

    @Override
    @Transactional
    public DrawingResponseDTO approveDrawing(Long id) {
        Drawing drawing = drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));

        drawing.setApproved(true);
        drawing.setModifiedAt(LocalDateTime.now());

        Progress progress = progressRepository.findByUserIdAndQuestId(drawing.getUser().getId(), drawing.getQuest().getId())
                .orElseGet(() -> createProgress(drawing.getUser(), drawing.getQuest()));

        if (!progress.isCompleted()) {
            progress.setCompleted(true);

            User user = drawing.getUser();
            user.setXp(user.getXp() + drawing.getQuest().getXpReward());
            user.setLevel(calculateLevel(user.getXp()));

            userRepository.save(user);
            progressRepository.save(progress);
        }

        return DrawingMapper.toDrawingResponseDTO(drawingRepository.save(drawing));
    }

    @Override
    public void deleteDrawing(Long id, String username) {
        Drawing drawing = drawingRepository.findByIdAndUserUsername(id, username)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
        drawingRepository.delete(drawing);
    }

    private Progress createProgress(User user, Quest quest) {
        Progress progress = new Progress();
        progress.setUser(user);
        progress.setQuest(quest);
        progress.setCompleted(false);
        progress.setAttempts(0);
        return progress;
    }

    private int calculateLevel(int xp) {
        return xp / 100;
    }
}
