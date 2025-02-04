package com.drawquest.service.impl;

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
    public Drawing findDrawingById(Long id) {
        return drawingRepository.findById(id).orElseThrow(() -> new RuntimeException("Drawing not found"));
    }

    @Override
    public Drawing saveDrawing(Drawing drawing) {
        return drawingRepository.save(drawing);
    }

    @Override
    public List<Drawing> getAllDrawings() {
        return drawingRepository.findAll();
    }
}

