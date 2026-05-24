package com.drawquest.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
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

    @Column(nullable = false)
    private int xpReward; // Experiencia otorgada al completar la misión


    @OneToMany(mappedBy = "quest")
    private List<Progress> progress; // Relación con el progreso de los usuarios

    @OneToMany(mappedBy = "quest")
    private List<Drawing> drawings; // Dibujos subidos para esta quest

}

