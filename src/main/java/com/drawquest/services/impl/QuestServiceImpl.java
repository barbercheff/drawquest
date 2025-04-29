package com.drawquest.services.impl;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.models.Quest;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.services.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestServiceImpl implements QuestService {
    @Autowired
    private QuestRepository questRepository;


    @Override
    public QuestResponseDTO getQuestById(Long id) {
        return questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + id + " not found"));
    }

    @Override
    public QuestResponseDTO createQuest(QuestCreateDTO questCreateDTO) {
        return questRepository.save(quest);
    }

    @Override
    public List<QuestResponseDTO> getAllQuests() {
        return questRepository.findAll();
    }

    @Override
    public Quest updateQuest(Long id, QuestCreateDTO questCreateDTO) {
        Quest existingQuest = getQuestById(id);

        existingQuest.setDescription(quest.getDescription());
        existingQuest.setDifficulty(quest.getDifficulty());
        existingQuest.setProgress(quest.getProgress());
        existingQuest.setTitle(quest.getTitle());

        return questRepository.save(existingQuest);
    }

    @Override
    public void deleteQuest(Long id) {
        Quest quest = getQuestById(id);
        questRepository.delete(quest);
    }
}

