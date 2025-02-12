package com.drawquest.controllers;

import com.drawquest.models.User;
import com.drawquest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Obtiene un usuario por ID", description = "Devuelve los datos de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Usuario encontrado")
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "Crea un nuevo usuario", description = "Crea un nuevo usuario en la base de datos")
    @ApiResponse(responseCode = "201", description = "Usuario creado con éxito", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content)
    @PostMapping
    public ResponseEntity<?> createUser(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a crear",
                    required = true, content = @Content(schema = @Schema(implementation = User.class)))
            @org.springframework.web.bind.annotation.RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @Operation(summary = "Actualiza un usuario existente", description = "Actualiza los datos de un usuario específico")
    @ApiResponse(responseCode = "200", description = "Usuario actualizado con éxito", content = @Content)
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                           @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a crear",
                                                   required = true, content = @Content(schema = @Schema(implementation = User.class)))
                                           @org.springframework.web.bind.annotation.RequestBody User user, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(userService.updateUser(id, user));
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
