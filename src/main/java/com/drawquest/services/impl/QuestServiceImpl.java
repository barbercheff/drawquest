package com.drawquest.services.impl;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.dtos.QuestResponseDTO;
import com.drawquest.dtos.QuestUpdateDTO;
import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.mappers.QuestMapper;
import com.drawquest.models.Quest;
import com.drawquest.repositories.QuestRepository;
import com.drawquest.services.QuestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestServiceImpl implements QuestService {

    private final QuestRepository questRepository;

    public QuestServiceImpl(QuestRepository questRepository) {
        this.questRepository = questRepository;
    }


    @Override
    public QuestResponseDTO getQuestById(Long id) {
        Quest quest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + id + " not found"));
        return QuestMapper.toQuestResponseDTO(quest);
    }

    @Override
    public QuestResponseDTO createQuest(QuestCreateDTO questCreateDTO) {
        Quest newQuest = QuestMapper.toQuestEntity(questCreateDTO);
        return QuestMapper.toQuestResponseDTO(questRepository.save(newQuest));
    }

    @Override
    public List<QuestResponseDTO> getAllQuests() {
        List<Quest> quests = questRepository.findAll();
        return quests.stream()
                .map(QuestMapper::toQuestResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QuestResponseDTO updateQuest(Long id, QuestUpdateDTO questUpdateDTO) {
        Quest existingQuest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + id + " not found"));

        existingQuest.setTitle(questUpdateDTO.getTitle());
        existingQuest.setDescription(questUpdateDTO.getDescription());
        existingQuest.setDifficulty(questUpdateDTO.getDifficulty());
        existingQuest.setXpReward(questUpdateDTO.getXpReward());

        return QuestMapper.toQuestResponseDTO(questRepository.save(existingQuest));
    }

    @Override
    public void deleteQuest(Long id) {
        Quest existingQuest = questRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quest with ID " + id + " not found"));
        questRepository.delete(existingQuest);
    }
}

