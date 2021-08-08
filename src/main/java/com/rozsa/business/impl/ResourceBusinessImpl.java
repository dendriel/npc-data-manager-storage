package com.rozsa.business.impl;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.configuration.ResourceProperties;
import com.rozsa.repository.DirectoryRepository;
import com.rozsa.repository.ResourceRepository;
import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageResourceInputStream;
import com.rozsa.s3.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ResourceBusinessImpl implements ResourceBusiness {
    private final ResourceRepository resourceRepository;
    private final DirectoryRepository directoryRepository;
    private final StorageService storage;
    private final ResourceProperties resourceProperties;

    @Override
    public StorageResourceInputStream get(String storageId) {
        return storage.getResource(storageId);
    }

    public Resource create(String name, ResourceType type, Long directoryId, MultipartFile multipartFile) {
        Optional<Directory> optDirectory = directoryRepository.findById(directoryId);
        if (optDirectory.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Directory not found");
        }

        Directory directory = optDirectory.get();
        String directoryStorageId = directory.getStorageId();
        String[] tokens = multipartFile.getOriginalFilename().split("\\.");
        String ext = tokens.length > 1 ? tokens[tokens.length - 1] : "";

        File file;
        try {
            Path tempFile = Files.createTempFile(null, null);
            System.out.println(tempFile);

            Path filePath = Files.write(tempFile, multipartFile.getBytes());
            file = filePath.toFile();

        } catch (IOException e) {
            log.error("Failed to create resource {} of type {} into directory {}", name, type, directoryId, e);
            return null;
        }

        String contentyType = multipartFile.getContentType();
        String storageId = storage.createResource(file, contentyType, ext, directoryStorageId);

        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        resource.setStorageId(storageId);
        resource.setDirectory(directory);

        return resourceRepository.save(resource);
    }

    @Override
    public void delete(Long id) {
        if (!resourceRepository.existsById(id)) {
            // done.
            return;
        }

        Optional<Resource> optResource = resourceRepository.findById(id);
        if (optResource.isEmpty()) {
            return;
        }

        Resource resource = optResource.get();
        storage.deleteResource(resource.getStorageId());

        resourceRepository.delete(resource);
    }

    @Override
    public URL getAccessUrl(Long id) {
        Optional<Resource> optResource = resourceRepository.findById(id);
        if (optResource.isEmpty()) {
            return null;
        }

        Resource resource = optResource.get();

        Date expiration = new Date();
        long expTimeMillis = Instant.now().toEpochMilli();
        expTimeMillis += 1000 * resourceProperties.getExpirationTimeInSec();
        expiration.setTime(expTimeMillis);

        return storage.getResourceAccessUrl(resource.getStorageId(), expiration);
    }

    @Override
    public ResourceType[] getTypes() {
        return ResourceType.values();
    }
}
