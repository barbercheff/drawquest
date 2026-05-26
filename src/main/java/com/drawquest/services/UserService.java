package com.drawquest.services;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserResponseDTO getUserById(Long id, String username);
    UserDetails loadUserByUsername(String username);
    UserResponseDTO getUserByUsername(String username);
    UserResponseDTO createUser(UserCreateDTO userCreateDTO);
    List<UserResponseDTO> getAllUsers();
    void deleteUser(Long id, String username);
}

