package com.rozsa.business;

import com.rozsa.repository.model.Resource;

import java.util.List;

public interface DirectoryBusiness {
    List<String> listAll();

    List<Resource> listResources(Long id);

    Long create(String name);

    void delete(Long id);
}
