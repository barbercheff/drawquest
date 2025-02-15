package com.drawquest.dtos;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;

import java.time.LocalDateTime;

public class DrawingUpdateDTO {
    private byte[] imageData;
    private LocalDateTime modifiedAt;
    private boolean approved;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
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

