package com.drawquest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressResponseDTO {
    private Long id;
    private Long userId;
    private Long questId;
    private boolean completed;
    private int attempts;
}
