package com.drawquest.controllers;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con usuarios")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Obtiene todos los usuarios", description = "Devuelve una lista de todos los usuarios registrados")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Obtiene un usuario por ID", description = "Devuelve los datos de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Crea un nuevo usuario", description = "Crea un nuevo usuario en la base de datos")
    @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a crear",
                    required = true, content = @Content(schema = @Schema(implementation = UserCreateDTO.class)))
            @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDTO));
    }

    @Operation(summary = "Actualiza un usuario existente", description = "Actualiza los datos de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                        @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                description = "Datos del usuario a actualizar",
                                                required = true,
                                                content = @Content(schema = @Schema(implementation = UserUpdateDTO.class))
                                        )
                                        @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO));
    }

    @Operation(summary = "Elimina un usuario", description = "Elimina un usuario de la base de datos mediante su ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado con éxito", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
