package com.drawquest.services;

import com.drawquest.models.Progress;

import java.util.List;

public interface ProgressService {
    Progress getProgressById(Long id);
    Progress createProgress(Progress progress);
    List<Progress> getAllProgress();
    Progress updateProgress(Long id, Progress progress);
    void deleteProgress(Long id);
}
