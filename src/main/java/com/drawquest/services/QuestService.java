package com.drawquest.services;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.models.Quest;

import java.util.List;

public interface QuestService {
    QuestResponseDTO getQuestById(Long id);
    QuestResponseDTO createQuest(QuestCreateDTO questCreateDTO);
    List<QuestResponseDTO> getAllQuests();
    QuestResponseDTO updateQuest(Long id, QuestCreateDTO questCreateDTO);
    void deleteQuest(Long id);
}
