package com.rozsa.controller;

import com.rozsa.repository.ResourceType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/storage/resource")
public interface ResourceController {

    @PostMapping("/upload")
    void create(@RequestParam String name,
                @RequestParam ResourceType type,
                @RequestParam Long directoryId,
                @RequestParam("file") MultipartFile file);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);


    @DeleteMapping("/")
    void deleteMany(@RequestBody List<Long> ids);
}
