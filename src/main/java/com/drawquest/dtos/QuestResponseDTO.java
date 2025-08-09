package com.drawquest.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestResponseDTO {

    private Long id;
    private String title;
    private String description;
    private int difficulty;
    private int xpReward;
}
