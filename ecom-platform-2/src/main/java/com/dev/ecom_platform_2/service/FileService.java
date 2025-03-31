package com.dev.ecom_platform_2.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String uploadImage(String path, MultipartFile image, List<String> allowedTypes) throws IOException;
}
