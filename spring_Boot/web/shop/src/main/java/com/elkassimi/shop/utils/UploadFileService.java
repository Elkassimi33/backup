package com.elkassimi.shop.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import com.elkassimi.shop.exception.*;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UploadFileService {

    @Value("${file.upload.base-path}")
    private String basePath;

    private Path baseStoragePath;

    @PostConstruct
    public void init() {
        this.baseStoragePath = Paths.get(basePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.baseStoragePath);
            log.info("Storage directory initialized at: {}", this.baseStoragePath);
        } catch (IOException e) {
            throw new FileStorageException("Could not initialize storage directory: " + this.baseStoragePath);
        }
    }

    public InputStream loadFileAsStream(String fileName, String pathCompletion) {
        // Security check: validate inputs before any path resolution
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }
        if (pathCompletion == null || pathCompletion.isBlank()) {
            throw new IllegalArgumentException("Path completion cannot be null or empty");
        }

        // Security check: reject dangerous filename patterns
        String cleanFileName = StringUtils.cleanPath(fileName);
        if (cleanFileName.contains("..") || cleanFileName.contains("/") || cleanFileName.contains("\\")) {
            throw new FileStorageException("Invalid file name: " + fileName);
        }

        Path filePath = getFilePath(cleanFileName, pathCompletion);

        // Security check: ensure resolved path is strictly inside base storage
        if (!filePath.toAbsolutePath().normalize().startsWith(baseStoragePath.toAbsolutePath().normalize())) {
            log.warn("Path traversal attempt detected for file: {}", fileName);
            throw new FileStorageException("Access denied: path traversal detected");
        }

        // Security check: ensure it's a regular file, not a symlink or directory
        if (!Files.exists(filePath)) {
            log.error("File not found: {}", filePath);
            throw new FileNotFoundException("File not found: " + fileName);
        }
        if (!Files.isRegularFile(filePath)) {
            log.warn("Attempt to access non-regular file: {}", filePath);
            throw new FileStorageException("Access denied: not a regular file: " + fileName);
        }
        if (!Files.isReadable(filePath)) {
            log.error("File exists but is not readable: {}", filePath);
            throw new FileStorageException("File is not readable: " + fileName);
        }

        try {
            InputStream inputStream = Files.newInputStream(filePath);
            log.info("File stream opened successfully: {}", filePath);
            return inputStream;
        } catch (IOException e) {
            log.error("Failed to open input stream for file: {}", filePath, e);
            throw new FileStorageException("Could not read file: " + fileName);
        }
    }

    public String storeMultipartFile(MultipartFile multipartFile, Long id, String pathCompletion) {
        if (multipartFile == null || multipartFile.isEmpty()) {
            throw new IllegalArgumentException("MultipartFile cannot be null or empty");
        }
        validateInput(id, pathCompletion);

        Path targetDirectory = resolveTargetDirectory(pathCompletion);

        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("File must have a valid original filename");
        }

        String fileName = generateFileName(id, StringUtils.cleanPath(originalFilename));
        Path targetLocation = targetDirectory.resolve(fileName);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            log.info("Multipart file stored successfully: {}", targetLocation);
            return fileName;
        } catch (IOException e) {
            log.error("Failed to store multipart file: {}", fileName, e);
            throw new FileStorageException("Could not store file: " + fileName);
        }
    }

    private void validateInput(Long id, String pathCompletion) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (pathCompletion == null || pathCompletion.isBlank()) {
            throw new IllegalArgumentException("Path completion cannot be null or empty");
        }
    }

    private Path resolveTargetDirectory(String pathCompletion) {
        String sanitizedPath = StringUtils.cleanPath(pathCompletion);
        if (sanitizedPath.contains("..")) {
            throw new FileStorageException("Invalid path completion contains '..' sequence");
        }

        Path targetPath = this.baseStoragePath.resolve(sanitizedPath).normalize();

        if (!targetPath.startsWith(this.baseStoragePath)) {
            throw new FileStorageException("Path traversal detected: " + pathCompletion);
        }

        try {
            Files.createDirectories(targetPath);
        } catch (IOException e) {
            throw new FileStorageException("Could not create directory: " + targetPath);
        }

        return targetPath;
    }

    // ✅ Fix #4: Cleaner method — extract extension first, then build the name clearly
    private String generateFileName(Long id, String originalFileName) {
        int lastDotIndex = originalFileName.lastIndexOf('.');
        String nameWithoutExtension = lastDotIndex > 0
                ? originalFileName.substring(0, lastDotIndex)
                : originalFileName;
        String extension = lastDotIndex > 0
                ? originalFileName.substring(lastDotIndex)
                : "";

        String timestamp = String.valueOf(System.currentTimeMillis());
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);

        return String.format("%d_%s_%s_%s%s", id, nameWithoutExtension, timestamp, uniqueId, extension);
    }

    private Path getFilePath(String fileName, String pathCompletion) {
        if (fileName == null || fileName.isBlank()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        String cleanFileName = StringUtils.cleanPath(fileName);
        if (cleanFileName.contains("..")) {
            throw new FileStorageException("Invalid file name contains '..' sequence");
        }

        Path targetDirectory = resolveTargetDirectory(pathCompletion);
        Path filePath = targetDirectory.resolve(cleanFileName).normalize();

        if (!filePath.startsWith(baseStoragePath)) {
            throw new FileStorageException("Path traversal detected for file: " + fileName);
        }

        return filePath;
    }

    public Path resolveFilePath(String fileName, String pathCompletion) {
        return getFilePath(fileName, pathCompletion);
    }
}