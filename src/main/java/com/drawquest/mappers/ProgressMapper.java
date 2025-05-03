package com.drawquest.mappers;

import com.drawquest.dtos.ProgressCreateDTO;
import com.drawquest.dtos.ProgressResponseDTO;
import com.drawquest.models.Progress;
import com.drawquest.models.Quest;
import com.drawquest.models.User;

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

    public static Progress toProgressEntity(ProgressCreateDTO progressCreateDTO, User user, Quest quest) {
        if (progressCreateDTO == null) {
            return null;
        }

        Progress progress = new Progress();
        progress.setUser(user);
        progress.setQuest(quest);
        progress.setCompleted(progressCreateDTO.isCompleted());

        return progress;
    }
}
