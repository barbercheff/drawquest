package com.drawquest.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private int level;  // Nivel del usuario en la progresión del juego

    @OneToMany(mappedBy = "user")
    private List<Progress> progress;

    // Getters y Setters
}

