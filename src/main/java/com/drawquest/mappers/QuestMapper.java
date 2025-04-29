package com.drawquest.mappers;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.dtos.QuestResponseDTO;
import com.drawquest.models.Quest;

public class QuestMapper {

    public static QuestResponseDTO toQuestResponseDTO(Quest quest) {
        if (quest == null) {
            return null;
        }

        return new QuestResponseDTO(
                quest.getId(),
                quest.getTitle(),
                quest.getDescription(),
                quest.getDifficulty(),
                quest.getXpReward()
        );
    }

    public static Quest toQuestEntity(QuestCreateDTO questCreateDTO) {
        if (questCreateDTO == null) {
            return null;
        }

        Quest quest = new Quest();
        quest.setTitle(questCreateDTO.getTitle());
        quest.setDescription(questCreateDTO.getDescription());
        quest.setDifficulty(questCreateDTO.getDifficulty());
        quest.setXpReward(questCreateDTO.getXpReward());
        return quest;
    }
}
