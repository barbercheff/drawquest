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

    @NotNull(message = "Quest ID is required")
    private Long questId;

    private boolean completed = false;

    @Min(value = 0, message = "Attempts cannot be negative")
    private int attempts = 0;
}
