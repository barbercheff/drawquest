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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Endpoints para autenticacion y registro")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario en la aplicacion")
    @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente")
    @ApiResponse(responseCode = "400", description = "Datos invalidos o error en la solicitud")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody(description = "Datos del usuario a registrar", required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserCreateDTO userCreateDTO) {

        userCreateDTO.setPassword(passwordEncoder.encode(userCreateDTO.getPassword()));
        return ResponseEntity.ok(userService.createUser(userCreateDTO));
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuario", description = "Genera un token JWT al iniciar sesion")
    @ApiResponse(responseCode = "200", description = "Inicio de sesion exitoso, devuelve el token JWT")
    @ApiResponse(responseCode = "401", description = "Credenciales invalidas")
    @ApiResponse(responseCode = "400", description = "Error en la solicitud")
    public ResponseEntity<Map<String, String>> login(
            @Valid @RequestBody(description = "Credenciales del usuario para iniciar sesion", required = true,
                    content = @Content(schema = @Schema(implementation = UserLoginDTO.class)))
            @org.springframework.web.bind.annotation.RequestBody UserLoginDTO userLoginDTO) {

        return ResponseEntity.ok(Map.of("token", authService.authenticate(userLoginDTO)));
    }
}
