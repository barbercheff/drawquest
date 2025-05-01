package com.drawquest.mappers;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.models.Drawing;
import com.drawquest.models.Quest;
import com.drawquest.models.User;

import java.time.LocalDateTime;

public class DrawingMapper {

    public static DrawingResponseDTO toDrawingResponseDTO(Drawing drawing) {
        if (drawing == null) {
            return null;
        }

        return new DrawingResponseDTO(
                drawing.getId(),
                drawing.getUser().getId(),
                drawing.getQuest().getId(),
                drawing.getImageUrl(),
                drawing.isApproved(),
                drawing.getModifiedAt(),
                drawing.getCreatedAt()

        );
    }

    public static Drawing toDrawingEntity(DrawingCreateDTO dto, User user, Quest quest) {
        if (dto == null || user == null || quest == null) {
            return null;
        }

        Drawing drawing = new Drawing();
        drawing.setUser(user);
        drawing.setQuest(quest);
        drawing.setImageUrl(dto.getImageUrl());
        drawing.setApproved(false);
        drawing.setCreatedAt(LocalDateTime.now());
        drawing.setModifiedAt(LocalDateTime.now());

        return drawing;
    }

}
