package com.drawquest.mappers;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.models.User;

import java.util.HashSet;

public class UserMapper {

    public static UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getLevel(),
                user.getXp()
        );
    }

    public static User toUserEntity(UserCreateDTO userCreateDTO) {
        if (userCreateDTO == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        user.setPassword(userCreateDTO.getPassword());
        user.setEmail(userCreateDTO.getEmail());
        user.setLevel(0);
        user.setXp(0);
        user.setProgress(null);
        user.setDrawings(null);
        user.setRoles(new HashSet<>());

        return user;
    }
}
