package com.drawquest.repositories;

import com.drawquest.models.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByUserUsername(String username);
    Optional<Progress> findByIdAndUserUsername(Long id, String username);
    Optional<Progress> findByUserIdAndQuestId(Long userId, Long questId);
}
