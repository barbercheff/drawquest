package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressCreateDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El ID de la quest es obligatorio")
    private Long questId;

    private boolean completed = false;

    @Min(value = 0, message = "Los intentos no pueden ser negativos")
    private int attempts = 0;
}
