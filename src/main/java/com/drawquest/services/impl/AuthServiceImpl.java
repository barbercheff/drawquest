package com.drawquest.services.impl;

import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.exceptions.UnauthorizedException;
import com.drawquest.models.User;
import com.drawquest.repositories.UserRepository;
import com.drawquest.security.JwtUtil;
import com.drawquest.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

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
                .orElseThrow(() -> {
                    logger.warn("Login failed for unknown username={}", userLoginDTO.getUsername());
                    return new UnauthorizedException("Invalid credentials");
                });

        if (!passwordEncoder.matches(userLoginDTO.getPassword(), existingUser.getPassword())) {
            logger.warn("Login failed for username={}", existingUser.getUsername());
            throw new UnauthorizedException("Invalid credentials");
        }

        logger.info("Login succeeded for userId={} username={}", existingUser.getId(), existingUser.getUsername());
        return jwtUtil.generateToken(existingUser.getUsername());
    }
}
