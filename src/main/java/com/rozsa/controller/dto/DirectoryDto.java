package com.rozsa.controller.dto;

import com.rozsa.repository.model.Directory;
import lombok.Data;

@Data
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
}
