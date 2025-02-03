package com.drawquest.model;

import jakarta.persistence.*;

@Entity
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "quest_id", nullable = false)
    private Quest quest;

    private boolean completed;

    private int attempts; // Intentos de completar la misión

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Quest getQuest() {
        return quest;
    }

    public void setQuest(Quest quest) {
        this.quest = quest;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }
}

