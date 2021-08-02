package com.rozsa.repository;

import com.rozsa.repository.model.Directory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DirectoryRepository extends CrudRepository<Directory, Long> {
    boolean existsDirectoryByName(String name);

    boolean existsDirectoryById(Long id);

    List<Directory> findAll();

    Directory findByName(String name);
}
