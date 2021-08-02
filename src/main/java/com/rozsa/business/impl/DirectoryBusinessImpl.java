package com.rozsa.business.impl;

import com.rozsa.business.DirectoryBusiness;
import com.rozsa.repository.DirectoryRepository;
import com.rozsa.repository.ResourceRepository;
import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@AllArgsConstructor
@Service
public class DirectoryBusinessImpl implements DirectoryBusiness {
    private final DirectoryRepository directoryRepository;
    private final ResourceRepository resourceRepository;
    private final StorageService storage;

    @Override
    public List<Directory> listAll() {
        List<Directory> directories = directoryRepository.findAll();

        // TODO: we could get this info while retrieving directory data by using SQL.
        for (Directory dir : directories) {
            Long count = resourceRepository.countAllByDirectory_Id(dir.getId());
            dir.setResourcesCount(count);
        }

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

    @Override
    public Directory findByName(String name) {
        Directory dir = directoryRepository.findByName(name);
        if (dir == null) {
            return null;
        }

        Long count = resourceRepository.countAllByDirectory_Id(dir.getId());
        dir.setResourcesCount(count);

        return dir;
    }
}
