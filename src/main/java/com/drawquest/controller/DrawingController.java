package com.drawquest.controller;

import com.drawquest.model.Drawing;
import com.drawquest.service.DrawingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drawings")
public class DrawingController {

    @Autowired
    private DrawingService drawingService;

    @GetMapping
    public ResponseEntity<List<Drawing>> getAllDrawings() {
        return ResponseEntity.ok(drawingService.getAllDrawings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drawing> getDrawingById(@PathVariable Long id) {
        Drawing drawing = drawingService.getDrawingById(id);
        return ResponseEntity.ok(drawing);
    }

    @PostMapping
    public ResponseEntity<Drawing> createDrawing(@RequestBody Drawing drawing) {
        return ResponseEntity.status(HttpStatus.CREATED).body(drawingService.createDrawing(drawing));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drawing> updateDrawing(@PathVariable Long id, @RequestBody Drawing drawing) {
        return ResponseEntity.ok(drawingService.updateDrawing(id, drawing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDrawing(@PathVariable Long id) {
        drawingService.deleteDrawing(id);
        return ResponseEntity.noContent().build();
    }
}

