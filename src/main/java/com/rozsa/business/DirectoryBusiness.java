package com.rozsa.business;

import com.rozsa.repository.model.Resource;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface DirectoryBusiness {
    List<String> listAll();

    List<Resource> listResources(@PathVariable("id") Long id);
}
