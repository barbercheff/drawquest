package com.drawquest.dto;

import com.drawquest.models.Progress;
import com.drawquest.models.Role;

import java.util.List;
import java.util.Set;

public class UserUpdateDTO {
    private int level;
    private List<Progress> progress;
    private Set<Role> roles;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Progress> getProgress() {
        return progress;
    }

    public void setProgress(List<Progress> progress) {
        this.progress = progress;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

