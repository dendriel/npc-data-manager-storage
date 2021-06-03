package com.rozsa.controller;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.repository.ResourceType;
import com.rozsa.s3.StorageResourceInputStream;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/resource")
public class ResourceController {
    private final ResourceBusiness business;

    @PostMapping("/upload")
    public void create(@RequestParam String name,
                       @RequestParam ResourceType type,
                       @RequestParam Long directoryId,
                       @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            // TODO: throw error
            return;
        }

        business.create(name, type, directoryId, file);
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

    @GetMapping("/types")
    public ResourceType[] getTypes() {
        return business.getTypes();
    }
}
