package com.drawquest.services;

import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.models.Drawing;
import java.util.List;

public interface DrawingService {
    Drawing getDrawingById(Long id);
    Drawing createDrawing(Drawing drawing);
    List<Drawing> getAllDrawings();
    Drawing updateDrawing(Long id, DrawingUpdateDTO drawing);
    void deleteDrawing(Long id);
}
