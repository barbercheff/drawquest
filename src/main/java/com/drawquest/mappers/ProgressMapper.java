package com.drawquest.mappers;

import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.models.Progress;

public class ProgressMapper {

    public static ProgressResponseDTO toProgressResponseDTO(Progress progress) {
        if (progress == null) {
            return null;
        }

        return new ProgressResponseDTO(
                progress.getId(),
                progress.getUser().getId(),
                progress.getQuest().getId(),
                progress.isCompleted(),
                progress.getAttempts()
        );
    }
}
