package com.drawquest.services;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    UserDetails loadUserByUsername(String username);
    User getUserByUsername(String username);
    User createUser(UserCreateDTO userCreateDTO);
    List<User> getAllUsers();
    User updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);
}

