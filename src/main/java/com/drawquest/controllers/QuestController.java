package com.drawquest.controllers;

import com.drawquest.dtos.QuestCreateDTO;
import com.drawquest.dtos.QuestResponseDTO;
import com.drawquest.dtos.QuestUpdateDTO;
import com.drawquest.services.QuestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quests")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    @GetMapping
    public ResponseEntity<List<QuestResponseDTO>> getAllQuests() {
        return ResponseEntity.ok(questService.getAllQuests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestResponseDTO> getQuestById(@PathVariable Long id) {
        return ResponseEntity.ok(questService.getQuestById(id));
    }

    @PostMapping
    public ResponseEntity<QuestResponseDTO> createQuest(@Valid @RequestBody QuestCreateDTO questCreateDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questService.createQuest(questCreateDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QuestResponseDTO> updateQuest(@PathVariable Long id, @Valid @RequestBody QuestUpdateDTO questUpdateDTO) {
        return ResponseEntity.ok(questService.updateQuest(id, questUpdateDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuest(@PathVariable Long id) {
        questService.deleteQuest(id);
        return ResponseEntity.noContent().build();
    }
}
