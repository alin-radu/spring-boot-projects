package com.dev.ecom_platform_2.service.impl;

import com.dev.ecom_platform_2.service.FileService;
import io.micrometer.common.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile image, List<String> allowedTypes) throws IOException {
        if (allowedTypes == null || allowedTypes.isEmpty()) {
            throw new IOException("Allowed types list cannot be null or empty.");
        }

        String originalFileName = image.getOriginalFilename();
        if (StringUtils.isBlank(originalFileName)) {
            throw new IOException("Invalid file name.");
        }

        String extension = getFileExtension(originalFileName, allowedTypes);
        if (extension.isEmpty()) {
            throw new IOException("File extension not found for file: " + originalFileName);
        }

        String newFileName = UUID.randomUUID() + "." + extension;

        Path directoryPath = Paths.get(path);
        Files.createDirectories(directoryPath);

        Path filePath = directoryPath.resolve(newFileName);

        try {
            Files.copy(image.getInputStream(), filePath);
        } catch (IOException e) {
            throw new IOException("Error writing file to disk for file: " + originalFileName, e);
        }

        return newFileName;
    }

    // HELPERS
    private String getFileExtension(String fileName, List<String> allowedTypes) throws IOException {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            String extension = fileName.substring(lastDotIndex + 1).toLowerCase();

            if (!allowedTypes.contains(extension)) {
                throw new IOException("Invalid file type for file: " + fileName + ". Allowed extensions are: " + String.join(", ", allowedTypes));
            }
            return extension;
        }
        return "";
    }
}
