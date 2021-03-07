package com.rozsa.business;

import com.rozsa.repository.ResourceType;
import org.springframework.web.multipart.MultipartFile;

public interface ResourceBusiness {

    void create(String name, ResourceType type, Long idDirectory, MultipartFile multipartFile);
}
