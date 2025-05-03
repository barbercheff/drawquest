package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ProgressCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID de la quest es obligatorio")
    private Long questId;

    private boolean completed = false;

    @Min(value = 0, message = "Los intentos no pueden ser negativos")
    private int attempts = 0;

    public ProgressCreateDTO() {
    }

    public ProgressCreateDTO(Long userId, Long questId, boolean completed, int attempts) {
        this.userId = userId;
        this.questId = questId;
        this.completed = completed;
        this.attempts = attempts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}
