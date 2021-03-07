package com.rozsa.s3;

import java.io.File;
import java.util.List;

public interface StorageService {
    List<String> listDirectories();

    List<String> listResources(String directory);

    String create(File file, String ext, String directory);
}
