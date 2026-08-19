package com.drawquest.services.impl;

import com.drawquest.config.UploadProperties;
import com.drawquest.exceptions.InvalidFileException;
import com.drawquest.services.DrawingImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalDrawingImageStorageService implements DrawingImageStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private static final Map<String, String> EXTENSIONS_BY_CONTENT_TYPE = Map.of(
            "image/jpeg", ".jpg",
            "image/png", ".png",
            "image/webp", ".webp",
            "image/gif", ".gif"
    );

    private final UploadProperties uploadProperties;

    public LocalDrawingImageStorageService(UploadProperties uploadProperties) {
        this.uploadProperties = uploadProperties;
    }

    @Override
    public String store(MultipartFile file) {
        validate(file);

        String filename = UUID.randomUUID() + EXTENSIONS_BY_CONTENT_TYPE.get(file.getContentType());
        Path uploadDir = Path.of(uploadProperties.getDrawingsDir()).toAbsolutePath().normalize();
        Path target = uploadDir.resolve(filename).normalize();

        if (!target.startsWith(uploadDir)) {
            throw new InvalidFileException("Invalid file path");
        }

        try {
            Files.createDirectories(uploadDir);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new InvalidFileException("Could not store uploaded file");
        }

        return uploadProperties.getDrawingsPublicPath() + "/" + filename;
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Drawing image is required");
        }

        if (!ALLOWED_CONTENT_TYPES.contains(file.getContentType())) {
            throw new InvalidFileException("Drawing image must be JPEG, PNG, WEBP, or GIF");
        }
    }
}
