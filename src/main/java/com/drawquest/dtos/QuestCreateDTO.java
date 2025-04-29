package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestCreateDTO {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    @Min(value = 1, message = "La dificultad debe ser al menos 1")
    private int difficulty;

    @NotNull(message = "La recompensa de experiencia es obligatoria")
    @Min(value = 0, message = "La recompensa de experiencia no puede ser negativa")
    private Integer xpReward;

    public QuestCreateDTO() {}

    public QuestCreateDTO(String title, String description, int difficulty, Integer xpReward) {
        this.title = title;
        this.description = description;
        this.difficulty = difficulty;
        this.xpReward = xpReward;
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

    public Integer getXpReward() {
        return xpReward;
    }

    public void setXpReward(Integer xpReward) {
        this.xpReward = xpReward;
    }
}
