package com.game.backend.services.file_upload.impl;

import com.game.backend.services.file_upload.FileUploadService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private static final String UPLOAD_DIR = "uploads/";

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Define the path where the file will be stored
        Path filePath = Paths.get(UPLOAD_DIR + fileName);

        // Write the file bytes to the path
        Files.write(filePath, file.getBytes());

        // Return the relative URL for the uploaded file
        return "/uploads/" + fileName;
    }
}
