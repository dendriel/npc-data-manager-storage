package com.rozsa.controller.dto;

import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Resource;


public class ResourceDto {
    private Long id;

    private String name;

    private ResourceType type;

    private String storageId;

    public static ResourceDto from(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.id = resource.getId();
        dto.name = resource.getName();
        dto.type = resource.getType();
        dto.storageId = resource.getStorageId();

        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
}
