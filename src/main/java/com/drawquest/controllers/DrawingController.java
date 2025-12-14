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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drawings")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Dibujos", description = "Operaciones relacionadas con dibujos")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @Operation(
            summary = "Obtiene todos los dibujos",
            description = "Devuelve una lista de todos los dibujos",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
            }
    )
    @GetMapping
    public ResponseEntity<List<DrawingResponseDTO>> getAllDrawings() {
        return ResponseEntity.ok(drawingService.getAllDrawings());
    }

    @Operation(
            summary = "Obtiene un dibujo por ID",
            description = "Devuelve los datos de un dibujo específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dibujo encontrado"),
                    @ApiResponse(responseCode = "404", description = "Dibujo no encontrado", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<DrawingResponseDTO> getDrawingById(@PathVariable Long id) {
        return ResponseEntity.ok(drawingService.getDrawingById(id));
    }

    @Operation(
            summary = "Crea un nuevo dibujo",
            description = "Permite a un usuario subir un dibujo para una quest",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dibujo creado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
            }
    )
    @PostMapping
    public ResponseEntity<DrawingResponseDTO> createDrawing(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del dibujo a crear",
            required = true, content = @Content(schema = @Schema(implementation = DrawingCreateDTO.class)))
                                                           @org.springframework.web.bind.annotation.RequestBody DrawingCreateDTO drawingCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(drawingService.createDrawing(drawingCreateDTO));
    }

    @Operation(
            summary = "Actualiza un dibujo existente",
            description = "Actualiza los datos de un dibujo específico",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dibujo actualizado con éxito", content = @Content(schema = @Schema(implementation = DrawingResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Dibujo no encontrado", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<DrawingResponseDTO> updateDrawing(@PathVariable Long id,
                                                 @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                         description = "Datos del dibujo a actualizar",
                                                         required = true,
                                                         content = @Content(schema = @Schema(implementation = DrawingUpdateDTO.class))
                                                 )
                                                 @RequestBody DrawingUpdateDTO drawingUpdateDTO) {

        return ResponseEntity.ok(drawingService.updateDrawing(id, drawingUpdateDTO));
    }

    @Operation(summary = "Elimina un dibujo", description = "Elimina un dibujo de la base de datos mediante su ID")
    @ApiResponse(responseCode = "204", description = "Dibujo eliminado con éxito", content = @Content)
    @ApiResponse(responseCode = "404", description = "Dibujo no encontrado", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrawing(@PathVariable Long id) {
        drawingService.deleteDrawing(id);
        return ResponseEntity.noContent().build();
    }
}

