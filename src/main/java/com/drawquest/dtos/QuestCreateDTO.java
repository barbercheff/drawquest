package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestCreateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Min(value = 1, message = "Difficulty must be at least 1")
    private int difficulty;

    @NotNull(message = "XP reward is required")
    @Min(value = 0, message = "XP reward cannot be negative")
    private Integer xpReward;
}
