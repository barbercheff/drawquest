package com.drawquest.services.impl;

import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.exceptions.UnauthorizedException;
import com.drawquest.security.JwtUtil;
import com.drawquest.services.AuthService;
import com.drawquest.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String authenticate(UserLoginDTO userLoginDTO) {
        UserResponseDTO existingUser = userService.getUserByUsername(userLoginDTO.getUsername());

        if (existingUser == null || !passwordEncoder.matches(userLoginDTO.getPassword(), userLoginDTO.getPassword())) {
            throw new UnauthorizedException("Credenciales incorrectas");
        }

        return jwtUtil.generateToken(existingUser.getUsername());
    }
}
