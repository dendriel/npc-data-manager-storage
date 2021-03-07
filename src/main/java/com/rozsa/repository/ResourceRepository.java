package com.rozsa.repository;

import com.rozsa.repository.model.Resource;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ResourceRepository extends CrudRepository<Resource, Long> {

    List<Resource> findAllByDirectoryId(Long id);
}
