package com.drawquest.controllers;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.services.AuthService;
import com.drawquest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Authentication and registration endpoints")
public class AuthController {

    private final UserService userService;

    private final AuthService authService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Register user", description = "Creates a new user")
    @ApiResponse(responseCode = "200", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody(description = "User registration data", required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserCreateDTO userCreateDTO) {

        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return ResponseEntity.ok(userService.createUser(userCreateDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Generates a JWT when login succeeds")
    @ApiResponse(responseCode = "200", description = "Login successful, returns the JWT")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @ApiResponse(responseCode = "400", description = "Invalid request data")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody(description = "User login credentials", required = true,
                    content = @Content(schema = @Schema(implementation = UserLoginDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserLoginDTO userLoginDTO) {

        return ResponseEntity.ok(Map.of("token", authService.authenticate(userLoginDTO)));
    }
}
