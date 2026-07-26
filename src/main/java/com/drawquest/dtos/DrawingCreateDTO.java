package com.drawquest.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingCreateDTO {

    @NotNull(message = "Quest ID is required")
    @Positive(message = "Quest ID must be positive")
    private Long questId;

    @NotBlank(message = "Image URL is required")
    @URL(message = "Must be a valid URL")
    private String imageUrl;
}
