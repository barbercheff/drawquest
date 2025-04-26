package com.drawquest.controllers;

import com.drawquest.dtos.DrawingCreateDTO;
import com.drawquest.dtos.DrawingUpdateDTO;
import com.drawquest.dtos.UserCreateDTO;
import com.drawquest.models.Drawing;
import com.drawquest.models.Quest;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/drawings")
@SecurityRequirement(name = "BearerAuth")
@Tag(name = "Dibujos", description = "Operaciones relacionadas con dibujos")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @GetMapping
    @Operation(summary = "Obtiene todos los dibujos", description = "Devuelve una lista de todos los dibujos")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    public ResponseEntity<List<Drawing>> getAllDrawings() {
        return ResponseEntity.ok(drawingService.getAllDrawings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un dibujo por ID", description = "Devuelve los datos de un dibujo específico")
    @ApiResponse(responseCode = "200", description = "Dibujo encontrado")
    @ApiResponse(responseCode = "404", description = "Dibujo no encontrado", content = @Content)
    public ResponseEntity<Drawing> getDrawingById(@PathVariable Long id) {
        Drawing drawing = drawingService.getDrawingById(id);
        return ResponseEntity.ok(drawing);
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Crea un nuevo dibujo",
            description = "Permite a un usuario subir un dibujo para una quest",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Dibujo creado con éxito"),
                    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos")
            }
    )
    public ResponseEntity<?> createDrawing(@Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del dibujo a crear",
            required = true, content = @Content(schema = @Schema(implementation = DrawingCreateDTO.class)))
                                                           @org.springframework.web.bind.annotation.RequestBody DrawingCreateDTO drawingCreateDTO) {

        if (drawingCreateDTO.getImageUrl().isEmpty()) {
            return ResponseEntity.badRequest().body("Debe incluirse una imagen válida.");
        }

        Drawing newDrawing = drawingService.createDrawing(drawingCreateDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newDrawing);
    }

    @Operation(summary = "Actualiza un dibujo existente", description = "Actualiza los datos de un dibujo específico")
    @ApiResponse(responseCode = "200", description = "Dibujo actualizado con éxito", content = @Content(schema = @Schema(implementation = Drawing.class)))
    @ApiResponse(responseCode = "400", description = "Datos de entrada no válidos", content = @Content)
    @ApiResponse(responseCode = "404", description = "Dibujo no encontrado", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDrawing(@PathVariable Long id,
                                                 @Valid @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                         description = "Datos del dibujo a actualizar",
                                                         required = true,
                                                         content = @Content(schema = @Schema(implementation = DrawingUpdateDTO.class))
                                                 )
                                                 @RequestBody DrawingUpdateDTO drawingUpdateDTO,
                                                 BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(errors);
        }

        Drawing updatedDrawing = drawingService.updateDrawing(id, drawingUpdateDTO);
        return ResponseEntity.ok(updatedDrawing);
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

