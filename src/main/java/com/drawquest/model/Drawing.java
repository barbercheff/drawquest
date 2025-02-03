package com.drawquest.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "drawings")
public class Drawing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    @Column(nullable = false)
    private String imageUrl; // URL donde se guarda el dibujo

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters y Setters
}

