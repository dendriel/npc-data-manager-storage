package com.rozsa.controller.dto;

import com.rozsa.repository.model.Directory;

public class DirectoryDto {
    private Long id;
    private String name;
    private String storageId;
    private Long resourcesCount;

    public static DirectoryDto from(Directory directory) {
        DirectoryDto dto = new DirectoryDto();
        dto.setId(directory.getId());
        dto.setName(directory.getName());
        dto.setStorageId(directory.getStorageId());
        dto.setResourcesCount(directory.getResourcesCount());

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

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }

    public Long getResourcesCount() {
        return resourcesCount;
    }

    public void setResourcesCount(Long resourcesCount) {
        this.resourcesCount = resourcesCount;
    }
}
