package com.drawquest.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "quests")
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    private int difficulty; // 1 = fácil, 2 = medio, 3 = difícil

    @OneToMany(mappedBy = "quest")
    private List<Progress> progress;

    // Getters y Setters
}

