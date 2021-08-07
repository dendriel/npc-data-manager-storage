package com.rozsa.business;

import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Resource;
import com.rozsa.s3.StorageResourceInputStream;
import org.springframework.web.multipart.MultipartFile;


public interface ResourceBusiness {
    StorageResourceInputStream get(String storageId);

    Resource create(String name, ResourceType type, Long idDirectory, MultipartFile multipartFile);

    void delete(Long id);

    ResourceType[] getTypes();
}
