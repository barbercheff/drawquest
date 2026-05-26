package com.drawquest.repositories;

import com.drawquest.models.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {
    List<Drawing> findByUserUsername(String username);
    Optional<Drawing> findByIdAndUserUsername(Long id, String username);
}
