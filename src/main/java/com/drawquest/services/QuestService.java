package com.drawquest.services;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.dtos.QuestResponseDTO;
import com.drawquest.dtos.QuestUpdateDTO;
import com.drawquest.models.Quest;

import java.util.List;

public interface QuestService {
    QuestResponseDTO getQuestById(Long id);
    QuestResponseDTO createQuest(QuestCreateDTO questCreateDTO);
    List<QuestResponseDTO> getAllQuests();
    QuestResponseDTO updateQuest(Long id, QuestUpdateDTO questUpdateDTO);
    void deleteQuest(Long id);
}
