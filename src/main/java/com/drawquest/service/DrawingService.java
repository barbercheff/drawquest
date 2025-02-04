package com.drawquest.service;

import com.drawquest.model.Drawing;

import java.util.List;

public interface DrawingService {
    Drawing findDrawingById(Long id);
    Drawing saveDrawing(Drawing drawing);
    List<Drawing> getAllDrawings();
}
