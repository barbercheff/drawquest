package com.drawquest.dtos;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

public class DrawingUpdateDTO {

    @NotNull(message = "La URL de la imagen es obligatoria")
    @URL(message = "Debe ser una URL válida")
    private String imageUrl;

    private LocalDateTime modifiedAt;

    private boolean approved;

    public DrawingUpdateDTO() {

    }

    public DrawingUpdateDTO(String imageUrl, LocalDateTime modifiedAt, boolean approved) {
        this.imageUrl = imageUrl;
        this.modifiedAt = modifiedAt;
        this.approved = approved;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}

