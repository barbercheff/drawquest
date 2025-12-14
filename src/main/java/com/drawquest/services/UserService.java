package com.drawquest.services;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.dtos.UserUpdateDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserResponseDTO getUserById(Long id);
    UserDetails loadUserByUsername(String username);
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);
}

