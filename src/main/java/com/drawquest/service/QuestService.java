package com.drawquest.service;

import com.drawquest.model.Quest;

import java.util.List;

public interface QuestService {
    Quest getQuestById(Long id);
    Quest createQuest(Quest quest);
    List<Quest> getAllQuests();
    Quest updateQuest(Long id, Quest quest);
    void deleteQuest(Long id);
}
