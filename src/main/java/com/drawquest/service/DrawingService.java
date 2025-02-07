package com.drawquest.service;

import com.drawquest.model.Drawing;
import java.util.List;

public interface DrawingService {
    Drawing getDrawingById(Long id);
    Drawing createDrawing(Drawing drawing);
    List<Drawing> getAllDrawings();
    Drawing updateDrawing(Long id, Drawing drawing);
    void deleteDrawing(Long id);
}
