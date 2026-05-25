package com.drawquest.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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
