package com.drawquest.controllers;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserLoginDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.models.User;
import com.drawquest.security.JwtUtil;
import com.drawquest.services.AuthService;
import com.drawquest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y registro")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en la aplicación")
    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos o error en la solicitud")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody(description = "Datos del usuario a registrar", required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserCreateDTO userCreateDTO) {

        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return ResponseEntity.ok(userService.createUser(userCreateDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Genera un token JWT al iniciar sesión")
    @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso, devuelve el token JWT")
    @ApiResponse(responseCode = "401", description = "Credenciales inválidas")
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody(description = "Credenciales del usuario para iniciar sesión", required = true,
                    content = @Content(schema = @Schema(implementation = UserLoginDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserLoginDTO userLoginDTO) {

            return ResponseEntity.ok(Map.of("token", authService.authenticate(userLoginDTO)));
    }
}
