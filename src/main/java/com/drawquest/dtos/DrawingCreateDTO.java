package com.drawquest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class DrawingCreateDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id de la quest es obligatorio")
    private Long questId;

    @Size(min = 1, message = "Debe incluirse una imagen válida")
    private MultipartFile imageData;

    public DrawingCreateDTO() {
    }

    public DrawingCreateDTO(Long userId, Long questId, MultipartFile imageData) {
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

    public MultipartFile getImageData() {
        return imageData;
    }

    public void setImageData(MultipartFile imageData) {
        this.imageData = imageData;
    }
}
