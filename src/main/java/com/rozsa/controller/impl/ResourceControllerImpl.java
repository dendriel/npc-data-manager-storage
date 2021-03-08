package com.rozsa.controller.impl;

import com.rozsa.business.ResourceBusiness;
import com.rozsa.controller.ResourceController;
import com.rozsa.repository.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage/resource")
public class ResourceControllerImpl implements ResourceController {
    private final ResourceBusiness business;

    @Autowired
    public ResourceControllerImpl(ResourceBusiness business) {
        this.business = business;
    }

    @Override
    public void create(@RequestParam String name,
                       @RequestParam ResourceType type,
                       @RequestParam Long directoryId,
                       @RequestParam("file") MultipartFile file
    ) {
        if (file.isEmpty()) {
            // TODO: throw error
            return;
        }

        business.create(name, type, directoryId, file);
    }

    @Override
    public void delete(Long id) {
        business.delete(id);
    }

    @Override
    public void deleteMany(@RequestBody List<Long> ids) {
        ids.forEach(this::delete);
    }
}
