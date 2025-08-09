package com.drawquest.dtos;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    @Min(value = 0, message = "El nivel no puede ser negativo")
    private int level;

    @Min(value = 0, message = "El XP no puede ser negativo")
    private int xp;

    private List<Progress> progress;

    private Set<Role> roles;
}

