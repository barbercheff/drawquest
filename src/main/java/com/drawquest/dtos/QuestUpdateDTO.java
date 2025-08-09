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
public class QuestUpdateDTO {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    private String description;

    @Min(value = 1, message = "La dificultad debe ser al menos 1")
    private int difficulty;

    @NotNull(message = "La recompensa de experiencia es obligatoria")
    @Min(value = 0, message = "La recompensa de experiencia no puede ser negativa")
    private Integer xpReward;
}
