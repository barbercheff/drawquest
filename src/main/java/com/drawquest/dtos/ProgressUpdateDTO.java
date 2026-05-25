package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressUpdateDTO {

    private Boolean completed;

    @Min(value = 0, message = "Attempts cannot be negative")
    private Integer attempts;
}
