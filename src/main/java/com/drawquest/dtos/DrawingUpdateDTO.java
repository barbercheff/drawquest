package com.drawquest.dtos;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class DrawingUpdateDTO {
    private byte[] imageData;
    private LocalDateTime modifiedAt;

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
}

