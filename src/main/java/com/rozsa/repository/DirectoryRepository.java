package com.rozsa.repository;

import com.rozsa.repository.model.Directory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DirectoryRepository extends CrudRepository<Directory, Long> {
    boolean existsDirectoryByName(String name);

    boolean existsDirectoryById(Long id);
}
