package com.drawquest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DrawingCreateDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id de la quest es obligatorio")
    private Long questId;

    @NotNull(message = "La imagen es obligatoria")
    private byte[] imageData;

    public DrawingCreateDTO() {
    }

    public DrawingCreateDTO(Long userId, Long questId, byte[] imageData) {
        this.userId = userId;
        this.questId = questId;
        this.imageData = imageData;
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

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
