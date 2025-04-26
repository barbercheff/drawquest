package com.drawquest.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

public class DrawingCreateDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id de la quest es obligatorio")
    private Long questId;

    @NotNull(message = "La URL de la imagen es obligatoria")
    @URL(message = "Debe ser una URL válida")
    private String imageUrl;

    public DrawingCreateDTO() {
    }

    public DrawingCreateDTO(Long userId, Long questId, String imageUrl) {
        this.userId = userId;
        this.questId = questId;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
