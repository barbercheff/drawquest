package com.drawquest.services.impl;

import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.exceptions.UnauthorizedException;
import com.drawquest.models.User;
import com.drawquest.repositories.UserRepository;
import com.drawquest.security.JwtUtil;
import com.drawquest.services.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String authenticate(UserLoginDTO userLoginDTO) {
        User existingUser = userRepository.findByUsername(userLoginDTO.getUsername())
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        return jwtUtil.generateToken(existingUser.getUsername());
    }
}
