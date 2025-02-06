package com.drawquest.service;

import com.drawquest.model.User;

import java.util.List;

public interface UserService {
    User getUserById(Long id);
    User createUser(User user);
    List<User> getAllUsers();
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}

