package com.drawquest.service.impl;

import com.drawquest.exception.ResourceNotFoundException;
import com.drawquest.model.Drawing;
import com.drawquest.repository.DrawingRepository;
import com.drawquest.service.DrawingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DrawingServiceImpl implements DrawingService {
    @Autowired
    private DrawingRepository drawingRepository;

    @Override
    public Drawing getDrawingById(Long id) {
        return drawingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Drawing with ID " + id + " not found"));
    }

    @Override
    public Drawing createDrawing(Drawing drawing) {
        return drawingRepository.save(drawing);
    }

    @Override
    public List<Drawing> getAllDrawings() {
        return drawingRepository.findAll();
    }

    @Override
    public Drawing updateDrawing(Long id, Drawing drawing) {
        Drawing existingDrawing = getDrawingById(id);

        existingDrawing.setCreatedAt(drawing.getCreatedAt());
        existingDrawing.setModifiedAt(drawing.getModifiedAt());
        existingDrawing.setUser(drawing.getUser());
        existingDrawing.setImageUrl(drawing.getImageUrl());
        existingDrawing.setQuest(drawing.getQuest());

        return drawingRepository.save(existingDrawing);
    }

    @Override
    public void deleteDrawing(Long id) {
        Drawing drawing = getDrawingById(id);
        drawingRepository.delete(drawing);
    }
}

