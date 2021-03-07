package com.rozsa.controller;

import com.rozsa.repository.ResourceType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/storage/resource")
public interface ResourceController {

    @PostMapping("/upload")
    void create(@RequestParam String name,
                @RequestParam ResourceType type,
                @RequestParam Long directoryId,
                @RequestParam("file") MultipartFile file);
}
