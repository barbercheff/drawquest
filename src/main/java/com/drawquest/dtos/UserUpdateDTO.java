package com.drawquest.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Min(value = 0, message = "Level cannot be negative")
    private int level;

    @Min(value = 0, message = "XP cannot be negative")
    private int xp;
}

