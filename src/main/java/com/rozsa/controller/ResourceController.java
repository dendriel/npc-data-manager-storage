package com.rozsa.controller;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.controller.dto.ResourceDto;
import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageResourceInputStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/resource")
public class ResourceController {
    private final ResourceBusiness business;

    @PostMapping("/upload")
    public ResponseEntity<ResourceDto> create(@RequestParam String name,
                                              @RequestParam ResourceType type,
                                              @RequestParam Long directoryId,
                                              @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            log.error("Received file is empty. Name: {}, type: {}, directoryId: {}", name, type, directoryId);
            return ResponseEntity.badRequest().build();
        }

        Resource resource = business.create(name, type, directoryId, file);
        if (resource == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        ResourceDto dto = ResourceDto.from(resource);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        business.delete(id);
    }

    @DeleteMapping
    public void deleteMany(@RequestBody List<Long> ids) {
        ids.forEach(this::delete);
    }

    @GetMapping
    public ResponseEntity<InputStreamResource> get(@RequestParam("storageId") String storageId) {
        StorageResourceInputStream resource = business.get(storageId);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentLength(resource.getContentLength())
                .contentType(resource.getMediaType())
                .body(new InputStreamResource(resource.getInputStream()));
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<String> getAccessUrl(@PathVariable("id") Long id) {
        URL url = business.getAccessUrl(id);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(url.toExternalForm());
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Void> download(@PathVariable("id") Long id) throws URISyntaxException {
        URL url = business.getAccessUrl(id);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders headers = new HttpHeaders();

        headers.setLocation(url.toURI());

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .headers(headers)
                .build();
    }

    @GetMapping("/types")
    public ResourceType[] getTypes() {
        return business.getTypes();
    }
}
