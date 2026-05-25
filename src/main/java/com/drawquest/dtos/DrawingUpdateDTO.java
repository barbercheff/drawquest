package com.drawquest.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DrawingUpdateDTO {

    @NotNull(message = "Image URL is required")
    @URL(message = "Must be a valid URL")
    private String imageUrl;

    private LocalDateTime modifiedAt;

    private boolean approved;
}
