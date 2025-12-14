package com.drawquest.services.impl;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.DrawingMapper;
import com.drawquest.models.Drawing;
import com.drawquest.models.Quest;
import com.drawquest.models.User;
import com.drawquest.repositories.DrawingRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.DrawingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DrawingServiceImpl implements DrawingService {

    private final DrawingRepository drawingRepository;

    private final UserRepository userRepository;

    private final QuestRepository questRepository;

    public DrawingServiceImpl(DrawingRepository drawingRepository, UserRepository userRepository, QuestRepository questRepository) {
        this.drawingRepository = drawingRepository;
        this.userRepository = userRepository;
        this.questRepository = questRepository;
    }

    @Override
    public DrawingResponseDTO getDrawingById(Long id) {
        Drawing drawing = drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
        return DrawingMapper.toDrawingResponseDTO(drawing);
    }

    @Override
    public DrawingResponseDTO createDrawing(DrawingCreateDTO drawingCreateDTO) {
        // Obtener referencias a las entidades relacionadas
        User user = userRepository.findById(drawingCreateDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + drawingCreateDTO.getUserId() + " not found"));

        Quest quest = questRepository.findById(drawingCreateDTO.getQuestId())
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + drawingCreateDTO.getQuestId() + " not found"));

        // Mapear DTO a entidad
        Drawing drawing = DrawingMapper.toDrawingEntity(drawingCreateDTO, user, quest);
        return DrawingMapper.toDrawingResponseDTO(drawingRepository.save(drawing));
    }

    @Override
    public List<DrawingResponseDTO> getAllDrawings() {
        List<Drawing> drawings = drawingRepository.findAll();
        return drawings.stream()
                .map(DrawingMapper::toDrawingResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DrawingResponseDTO updateDrawing(Long id, DrawingUpdateDTO drawingUpdateDTO) {
        Drawing existingDrawing = drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));

        if (drawingUpdateDTO.getImageUrl() != null) {
            existingDrawing.setImageUrl(drawingUpdateDTO.getImageUrl());
        }

        if (drawingUpdateDTO.getModifiedAt() != null) {
            existingDrawing.setModifiedAt(drawingUpdateDTO.getModifiedAt());
        }

        existingDrawing.setApproved(drawingUpdateDTO.isApproved());

        return DrawingMapper.toDrawingResponseDTO(drawingRepository.save(existingDrawing));
    }

    @Override
    public void deleteDrawing(Long id) {
        Drawing drawing = drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
        drawingRepository.delete(drawing);
    }
}

