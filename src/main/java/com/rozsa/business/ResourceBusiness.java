package com.rozsa.business;

import com.rozsa.repository.ResourceType;
import com.rozsa.s3.StorageResourceInputStream;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface ResourceBusiness {
    StorageResourceInputStream get(String storageId);

    void create(String name, ResourceType type, Long idDirectory, MultipartFile multipartFile);

    void delete(Long id);

    ResourceType[] getTypes();
}
