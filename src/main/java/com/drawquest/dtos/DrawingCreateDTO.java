package com.drawquest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingCreateDTO {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El id de la quest es obligatorio")
    private Long questId;

    @NotBlank(message = "La URL de la imagen es obligatoria")
    @URL(message = "Debe ser una URL válida")
    private String imageUrl;
}
