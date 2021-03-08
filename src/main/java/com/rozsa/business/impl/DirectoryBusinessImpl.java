package com.rozsa.business.impl;

import com.rozsa.business.DirectoryBusiness;
import com.rozsa.repository.DirectoryRepository;
import com.rozsa.repository.ResourceRepository;
import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Override
    public Long create(@RequestParam String name) {
        if (directoryRepository.existsDirectoryByName(name)) {
            return -1L;
        }

        Directory directory = new Directory();
        directory.setName(name);

        String storageId = storage.createDirectory(name);
        directory.setStorageId(storageId);

        directoryRepository.save(directory);

        return directory.getId();
    }

    @Override
    public void delete(Long id) {
        if (!directoryRepository.existsDirectoryById(id)) {
            // done.
            return;
        }

        if (resourceRepository.countAllByDirectory_Id(id) > 0 ) {
            // TODO: throw an error
            return;
        }

        directoryRepository.deleteById(id);
    }
}
