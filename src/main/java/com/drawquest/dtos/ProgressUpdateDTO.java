package com.drawquest.dtos;

import jakarta.validation.constraints.Min;

public class ProgressUpdateDTO {

    private Boolean completed;

    @Min(value = 0, message = "Los intentos no pueden ser negativos")
    private Integer attempts;

    public ProgressUpdateDTO() {
    }

    public ProgressUpdateDTO(Boolean completed, Integer attempts) {
        this.completed = completed;
        this.attempts = attempts;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }
}
