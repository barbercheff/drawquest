package com.drawquest.controllers;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingResponseDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.services.DrawingService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/drawings")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Drawings", description = "Drawing operations")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @Operation(
            summary = "Get all drawings",
            description = "Returns all drawings",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List retrieved successfully")
            }
    )
    @GetMapping
    public ResponseEntity<List<DrawingResponseDTO>> getAllDrawings(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(drawingService.getAllDrawings(userDetails.getUsername()));
    }

    @Operation(
            summary = "Get drawing by ID",
            description = "Returns a specific drawing",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Drawing found"),
                    @ApiResponse(responseCode = "404", description = "Drawing not found", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DrawingResponseDTO> getDrawingById(@PathVariable Long id,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(drawingService.getDrawingById(id, userDetails.getUsername()));
    }

    @Operation(
            summary = "Create drawing",
            description = "Allows the authenticated user to submit a drawing for a quest",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Drawing created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data")
            }
    )
    @PostMapping
    public ResponseEntity<DrawingResponseDTO> createDrawing(
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Drawing creation data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DrawingCreateDTO.class)))
            @RequestBody DrawingCreateDTO drawingCreateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(drawingService.createDrawing(drawingCreateDTO, userDetails.getUsername()));
    }

    @Operation(
            summary = "Update drawing",
            description = "Updates a specific drawing",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Drawing updated successfully", content = @Content(schema = @Schema(implementation = DrawingResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Drawing not found", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DrawingResponseDTO> updateDrawing(
            @PathVariable Long id,
            @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Drawing update data",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DrawingUpdateDTO.class)))
            @RequestBody DrawingUpdateDTO drawingUpdateDTO,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(drawingService.updateDrawing(id, drawingUpdateDTO, userDetails.getUsername()));
    }

    @Operation(summary = "Delete drawing", description = "Deletes a drawing by ID")
    @ApiResponse(responseCode = "204", description = "Drawing deleted successfully", content = @Content)
    @ApiResponse(responseCode = "404", description = "Drawing not found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrawing(@PathVariable Long id,
                                              @AuthenticationPrincipal UserDetails userDetails) {
        drawingService.deleteDrawing(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
