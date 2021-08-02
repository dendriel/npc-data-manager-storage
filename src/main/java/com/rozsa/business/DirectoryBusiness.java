package com.rozsa.business;

import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;

import java.util.List;

public interface DirectoryBusiness {
    List<Directory> listAll();

    List<Resource> listResources(Long id);

    Long create(String name);

    void delete(Long id);

    Directory findByName(String name);
}
