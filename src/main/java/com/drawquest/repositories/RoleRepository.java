package com.drawquest.repositories;

import java.util.Optional;

import com.drawquest.enums.ERole;
import com.drawquest.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
