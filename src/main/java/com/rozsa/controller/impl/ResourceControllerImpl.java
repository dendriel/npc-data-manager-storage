package com.rozsa.controller.impl;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.controller.ResourceController;
import com.rozsa.repository.ResourceType;
import com.rozsa.s3.StorageResourceInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage/resource")
public class ResourceControllerImpl implements ResourceController {
    private final ResourceBusiness business;

    @Autowired
    public ResourceControllerImpl(ResourceBusiness business) {
        this.business = business;
    }

    @Override
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

    @Override
    public void delete(Long id) {
        business.delete(id);
    }

    @Override
    public void deleteMany(@RequestBody List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Override
    public ResponseEntity<InputStreamResource> get(@RequestParam("storageId") String storageId) {
        StorageResourceInputStream resource = business.get(storageId);

        return ResponseEntity.ok()
                .contentLength(resource.getContentLength())
                .contentType(resource.getMediaType())
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
