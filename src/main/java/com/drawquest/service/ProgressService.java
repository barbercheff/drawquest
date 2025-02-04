package com.drawquest.service;

import com.drawquest.model.Progress;

import java.util.List;

public interface ProgressService {
    Progress findProgressById(Long id);
    Progress saveProgress(Progress progress);
    List<Progress> getAllProgress();
}
