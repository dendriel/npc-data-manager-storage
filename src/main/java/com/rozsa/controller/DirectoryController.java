package com.rozsa.controller;

import com.rozsa.controller.dto.ResourceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/storage/directory")
public interface DirectoryController {
    @PostMapping
    @ResponseBody
    Long create(@RequestParam("name") String name);

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);

    @GetMapping("/all")
    @ResponseBody
    List<String> getAll();

    @GetMapping("/{id}/list")
    @ResponseBody
    List<ResourceDto> listResources(@PathVariable("id") Long id);
}
