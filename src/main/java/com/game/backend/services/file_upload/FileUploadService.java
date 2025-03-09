package com.game.backend.services.file_upload;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadImage(MultipartFile file) throws IOException;
}
