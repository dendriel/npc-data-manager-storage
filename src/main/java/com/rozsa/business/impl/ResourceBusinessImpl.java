package com.rozsa.business.impl;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.repository.DirectoryRepository;
import com.rozsa.repository.ResourceRepository;
import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageResourceInputStream;
import com.rozsa.s3.StorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class ResourceBusinessImpl implements ResourceBusiness {
    private final ResourceRepository resourceRepository;
    private final DirectoryRepository directoryRepository;
    private final StorageService storage;

    public ResourceBusinessImpl(
            ResourceRepository resourceRepository,
            DirectoryRepository directoryRepository,
            StorageService storage
        ) {
        this.resourceRepository = resourceRepository;
        this.directoryRepository = directoryRepository;
        this.storage = storage;
    }

    @Override
    public StorageResourceInputStream get(String storageId) {
        return storage.getResource(storageId);
    }

    public void create(String name, ResourceType type, Long directoryId, MultipartFile multipartFile) {
        Optional<Directory> optDirectory = directoryRepository.findById(directoryId);
        if (optDirectory.isEmpty()) {
            // Throw error?
            return;
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
            e.printStackTrace();
            return;
        }

        String contentyType = multipartFile.getContentType();
        String storageId = storage.createResource(file, contentyType, ext, directoryStorageId);

        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        resource.setStorageId(storageId);
        resource.setDirectory(directory);

        resourceRepository.save(resource);
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
    public ResourceType[] getTypes() {
        return ResourceType.values();
    }
}
