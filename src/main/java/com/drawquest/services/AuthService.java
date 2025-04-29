package com.drawquest.services;

import com.drawquest.dtos.UserLoginDTO;

public interface AuthService {
    String authenticate(UserLoginDTO userLoginDTO);
}
