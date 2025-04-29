package com.drawquest.dtos;

import com.drawquest.models.Drawing;
import com.drawquest.models.Progress;

import java.util.List;

public class QuestResponseDTO {

    private Long id;
    private String title;
    private String description;
    private int difficulty;
    private int xpReward;

    public QuestResponseDTO() {
    }

    public QuestResponseDTO(Long id, String title, String description, int difficulty, int xpReward) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.xpReward = xpReward;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public int getXpReward() {
        return xpReward;
    }

    public void setXpReward(int xpReward) {
        this.xpReward = xpReward;
    }
}
