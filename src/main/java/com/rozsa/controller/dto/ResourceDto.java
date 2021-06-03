package com.rozsa.controller.dto;

import com.rozsa.repository.ResourceType;
import com.rozsa.repository.model.Resource;
import lombok.Data;


@Data
public class ResourceDto {
    private Long id;
    private String name;
    private ResourceType type;
    private String storageId;

    // TODO: use mapstruct.Mapper
    public static ResourceDto from(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.id = resource.getId();
        dto.name = resource.getName();
        dto.type = resource.getType();
        dto.storageId = resource.getStorageId();

        return dto;
    }
}
