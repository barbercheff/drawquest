package com.drawquest.dtos;

public class ProgressResponseDTO {
    private Long id;
    private Long userId;
    private Long questId;
    private boolean completed;
    private int attempts;

    public ProgressResponseDTO() {
    }

    public ProgressResponseDTO(Long id, Long userId, Long questId, boolean completed, int attempts) {
        this.id = id;
        this.userId = userId;
        this.questId = questId;
        this.completed = completed;
        this.attempts = attempts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
