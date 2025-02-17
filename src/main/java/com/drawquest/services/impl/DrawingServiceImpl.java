package com.drawquest.services.impl;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.models.Drawing;
import com.drawquest.models.Quest;
import com.drawquest.models.User;
import com.drawquest.repositories.DrawingRepository;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.repositories.UserRepository;
import com.drawquest.services.DrawingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DrawingServiceImpl implements DrawingService {
    @Autowired
    private DrawingRepository drawingRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestRepository questRepository;

    @Override
    public Drawing getDrawingById(Long id) {
        return drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
    }

    @Override
    public Drawing createDrawing(DrawingCreateDTO drawingCreateDTO) {
        Drawing newDrawing = new Drawing();
        User user = userRepository.getReferenceById(drawingCreateDTO.getUserId());
        Quest quest = questRepository.getReferenceById(drawingCreateDTO.getQuestId());

        newDrawing.setUser(user);
        newDrawing.setQuest(quest);
        try {
            newDrawing.setImageData(drawingCreateDTO.getImageData().getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return drawingRepository.save(newDrawing);
    }

    @Override
    public List<Drawing> getAllDrawings() {
        return drawingRepository.findAll();
    }

    @Override
    public Drawing updateDrawing(Long id, DrawingUpdateDTO drawing) {
        Drawing existingDrawing = getDrawingById(id);

        existingDrawing.setModifiedAt(drawing.getModifiedAt());
        existingDrawing.setImageData(drawing.getImageData());

        return drawingRepository.save(existingDrawing);
    }

    @Override
    public void deleteDrawing(Long id) {
        Drawing drawing = getDrawingById(id);
        drawingRepository.delete(drawing);
    }
}

