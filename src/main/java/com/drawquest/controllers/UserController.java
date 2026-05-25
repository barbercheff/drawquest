package com.drawquest.controllers;

import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.dtos.UserResponseDTO;
import com.drawquest.dtos.UserUpdateDTO;
import com.drawquest.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Users", description = "User operations")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Get all users", description = "Returns all registered users")
    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "Returns a specific user")
    @ApiResponse(responseCode = "200", description = "User found")
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Create user", description = "Creates a user in the database")
    @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User creation data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class)))
            @RequestBody UserCreateDTO userCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userCreateDTO));
    }

    @Operation(summary = "Update user", description = "Updates a specific user")
    @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User update data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateDTO.class)))
            @RequestBody UserUpdateDTO userUpdateDTO) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDTO));
    }

    @Operation(summary = "Delete user", description = "Deletes a user by ID")
    @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content)
    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
