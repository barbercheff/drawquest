package com.drawquest.service.impl;

import com.drawquest.model.Quest;
import com.drawquest.repository.QuestRepository;
import com.drawquest.service.QuestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestServiceImpl implements QuestService {
    @Autowired
    private QuestRepository questRepository;


    @Override
    public Quest findQuestById(Long id) {
        return questRepository.findById(id).orElseThrow(() -> new RuntimeException("Quest not found"));
    }

    @Override
    public Quest saveQuest(Quest quest) {
        return questRepository.save(quest);
    }

    @Override
    public List<Quest> getAllQuests() {
        return questRepository.findAll();
    }
}

