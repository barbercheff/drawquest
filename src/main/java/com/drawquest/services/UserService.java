package com.drawquest.services;

import com.drawquest.models.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    UserDetails loadUserByUsername(String username);
    User getUserByUsername(String username);
    User createUser(User user);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}

