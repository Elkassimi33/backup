package com.elkassimi.shop.controller;

import com.elkassimi.shop.dtos.responseDtos.CategoryResponseDto;
import com.elkassimi.shop.utils.UploadFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class FileController {

    private final UploadFileService fileService;

    @GetMapping("/categories/{imageName}")
    public ResponseEntity<Resource> getCategoryImage(@PathVariable String imageName) {
        log.info("REST request to get category image: {}", imageName);
        return buildImageResponse(imageName, "categories");
    }

    @GetMapping("/products/{imageName}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String imageName) {
        log.info("REST request to get product image: {}", imageName);
        return buildImageResponse(imageName, "products");
    }

    private ResponseEntity<Resource> buildImageResponse(String imageName, String folder) {
        InputStream inputStream = fileService.loadFileAsStream(imageName, folder);
        InputStreamResource resource = new InputStreamResource(inputStream);
        MediaType mediaType = resolveMediaType(imageName);

        // ✅ Read file size for Content-Length so Flutter can show real load progress
        long contentLength = -1;
        try {
            Path filePath = fileService.resolveFilePath(imageName, folder);
            contentLength = Files.size(filePath);
        } catch (IOException e) {
            log.warn("Could not determine file size for: {}", imageName);
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .contentLength(contentLength)  // ✅ -1 means unknown (still works, just no progress)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageName + "\"")
                .header(HttpHeaders.CACHE_CONTROL, "max-age=86400") // ✅ cache for 1 day
                .body(resource);
    }

    private MediaType resolveMediaType(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".png"))  return MediaType.IMAGE_PNG;
        if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) return MediaType.IMAGE_JPEG;
        if (lower.endsWith(".gif")) return MediaType.IMAGE_GIF;
        if (lower.endsWith(".webp")) return MediaType.parseMediaType("image/webp");
        if (lower.endsWith(".svg"))  return MediaType.parseMediaType("image/svg+xml");
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
