package com.drawquest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingResponseDTO {
    private Long id;
    private Long userId;
    private Long questId;
    private String imageUrl;
    private boolean approved;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
