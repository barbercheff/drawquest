package com.drawquest.service;

import com.drawquest.model.Quest;

import java.util.List;

public interface QuestService {
    Quest findQuestById(Long id);
    Quest saveQuest(Quest quest);
    List<Quest> getAllQuests();
}
