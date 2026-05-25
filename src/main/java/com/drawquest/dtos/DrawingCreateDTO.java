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

    @NotNull(message = "Quest ID is required")
    private Long questId;

    @NotBlank(message = "Image URL is required")
    @URL(message = "Must be a valid URL")
    private String imageUrl;
}
