package com.drawquest.service.impl;

import com.drawquest.model.Progress;
import com.drawquest.repository.ProgressRepository;
import com.drawquest.service.ProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgressServiceImpl implements ProgressService {
    @Autowired
    private ProgressRepository progressRepository;

    @Override
    public Progress findProgressById(Long id) {
        return progressRepository.findById(id).orElseThrow(() -> new RuntimeException("Progress not found"));
    }

    @Override
    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }

    @Override
    public List<Progress> getAllProgress() {
        return progressRepository.findAll();
    }
}

