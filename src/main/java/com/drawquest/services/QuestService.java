package com.drawquest.services;

import com.drawquest.models.Quest;

import java.util.List;

public interface QuestService {
    Quest getQuestById(Long id);
    Quest createQuest(Quest quest);
    List<Quest> getAllQuests();
    Quest updateQuest(Long id, Quest quest);
    void deleteQuest(Long id);
}
