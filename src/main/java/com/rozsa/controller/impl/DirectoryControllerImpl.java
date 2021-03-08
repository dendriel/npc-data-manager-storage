package com.rozsa.controller.impl;

import com.rozsa.business.DirectoryBusiness;
import com.rozsa.controller.DirectoryController;
import com.rozsa.controller.dto.ResourceDto;
import com.rozsa.repository.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/storage/directory")
public class DirectoryControllerImpl implements DirectoryController {

    private final DirectoryBusiness business;

    @Autowired
    public DirectoryControllerImpl(DirectoryBusiness business) {
        this.business = business;
    }

    public List<String> getAll() {
        return business.listAll();
    }

    public Long create(@RequestParam("name") String name) {
        return business.create(name);
    }

    public void delete(@PathVariable("id") Long id) {
        business.delete(id);
    }

    public List<ResourceDto> listResources(@PathVariable("id") Long id) {
        List<Resource> resources = business.listResources(id);
        List<ResourceDto> dtos = resources.stream()
                                            .map(ResourceDto::from)
                                            .collect(Collectors.toList());
        return dtos;
    }
}
