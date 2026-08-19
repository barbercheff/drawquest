package com.drawquest.services;

import org.springframework.web.multipart.MultipartFile;

public interface DrawingImageStorageService {

    String store(MultipartFile file);
}
