package com.drawquest.services.impl;

import com.drawquest.exceptions.ResourceNotFoundException;
import com.drawquest.models.Progress;
import com.drawquest.repositories.ProgressRepository;
import com.drawquest.services.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {
    @Autowired
    private ProgressRepository progressRepository;

    @Override
    public Progress getProgressById(Long id) {
        return progressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Progress with ID " + id + " not found"));
    }

    @Override
    public Progress createProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    @Override
    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }

    @Override
    public Progress updateProgress(Long id, Progress progress) {
        Progress existingProgress = getProgressById(id);

        existingProgress.setAttempts(progress.getAttempts());
        existingProgress.setCompleted(progress.isCompleted());
        existingProgress.setUser(progress.getUser());
        existingProgress.setQuest(progress.getQuest());

        return progressRepository.save(existingProgress);
    }

    @Override
    public void deleteProgress(Long id) {
        Progress progress = getProgressById(id);
        progressRepository.delete(progress);
    }
}

