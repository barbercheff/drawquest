package com.drawquest.dtos;

public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private int level;
    private int xp;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String username, String email, int level, int xp) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.level = level;
        this.xp = xp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }
}
