package com.drawquest.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore // Oculta el ID en Swagger y en cualquier respuesta JSON
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private int level;  // Nivel del usuario en la progresión del juego

    private int xp; // Experiencia acumulada

    @OneToMany(mappedBy = "user")
    private List<Progress> progress; // Relación con el progreso de quests

    @OneToMany(mappedBy = "user")
    private List<Drawing> drawings; // Dibujos del usuario

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {

    }

    public User(Long id, String username, String password, String email, int level, int xp, List<Progress> progress,
                List<Drawing> drawings, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.level = level;
        this.xp = xp;
        this.progress = progress;
        this.drawings = drawings;
        this.roles = roles;
    }

}

