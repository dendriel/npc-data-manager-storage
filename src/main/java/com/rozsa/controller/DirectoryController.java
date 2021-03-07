package com.rozsa.controller;

import com.rozsa.controller.dto.ResourceDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/storage/directory")
public interface DirectoryController {

    @GetMapping("/all")
    List<String> listAll();

    @DeleteMapping("/{id}")
    void delete(@PathVariable("id") Long id);

    @GetMapping("/{id}/list")
    @ResponseBody
    List<ResourceDto> listResources(@PathVariable("id") Long id);

}
