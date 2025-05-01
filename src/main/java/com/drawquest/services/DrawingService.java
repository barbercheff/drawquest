package com.drawquest.services;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import java.util.List;

public interface DrawingService {
    DrawingResponseDTO getDrawingById(Long id);
    DrawingResponseDTO createDrawing(DrawingCreateDTO drawingCreateDTO);
    List<DrawingResponseDTO> getAllDrawings();
    DrawingResponseDTO updateDrawing(Long id, DrawingUpdateDTO drawing);
    void deleteDrawing(Long id);
}
