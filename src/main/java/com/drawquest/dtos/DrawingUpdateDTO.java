package com.drawquest.dtos;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;

import java.time.LocalDateTime;

public class DrawingUpdateDTO {
    private String imageUrl;
    private LocalDateTime modifiedAt;
    private boolean approved;

    public String getImageData() {
        return imageUrl;
    }

    public void setImageData(String imageData) {
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

