package com.rozsa.controller;

import com.rozsa.business.DirectoryBusiness;
import com.rozsa.controller.dto.DirectoryDto;
import com.rozsa.controller.dto.ResourceDto;
import com.rozsa.repository.model.Directory;
import com.rozsa.repository.model.Resource;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/directory")
public class DirectoryController {
    private final DirectoryBusiness business;

    @GetMapping("/all")
    @ResponseBody
    public List<DirectoryDto> getAll() {
        List<Directory> directories = business.listAll();
        List<DirectoryDto> dtos = directories.stream()
                .map(DirectoryDto::from)
                .collect(Collectors.toList());

        return dtos;
    }

    @PostMapping
    @ResponseBody
    public Long create(@RequestParam("name") String name) {
        return business.create(name);
    }

    @GetMapping("/find")
    public ResponseEntity<DirectoryDto> getByName(@RequestParam("name") String name) {
        Directory directory = business.findByName(name);
        if (directory == null) {
            return ResponseEntity.notFound().build();
        }

        DirectoryDto dto = DirectoryDto.from(business.findByName(name));
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        business.delete(id);
    }

    @GetMapping("/{id}/list")
    @ResponseBody
    public List<ResourceDto> listResources(@PathVariable("id") Long id) {
        List<Resource> resources = business.listResources(id);
        return resources.stream()
                .map(ResourceDto::from)
                .collect(Collectors.toList());
    }
}
