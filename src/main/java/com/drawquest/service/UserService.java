package com.drawquest.service;

import com.drawquest.model.User;

import java.util.List;

public interface UserService {
    User findUserById(Long id);
    User saveUser(User user);
    List<User> getAllUsers();
}

