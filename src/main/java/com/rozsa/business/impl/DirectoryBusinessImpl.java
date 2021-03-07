package com.rozsa.business.impl;

import com.rozsa.business.DirectoryBusiness;
import com.rozsa.repository.DirectoryRepository;
import com.rozsa.repository.ResourceRepository;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DirectoryBusinessImpl implements DirectoryBusiness {
    private final DirectoryRepository directoryRepository;

    private final ResourceRepository resourceRepository;

    private final StorageService storage;

    @Autowired
    public DirectoryBusinessImpl(DirectoryRepository directoryRepository, ResourceRepository resourceRepository, StorageService storage) {
        this.directoryRepository = directoryRepository;
        this.resourceRepository = resourceRepository;
        this.storage = storage;
    }

    @Override
    public List<String> listAll() {
        List<String> directories = storage.listDirectories();
        return directories;
    }

    @Override
    public List<Resource> listResources(Long id) {
        return resourceRepository.findAllByDirectoryId(id);
    }
}
