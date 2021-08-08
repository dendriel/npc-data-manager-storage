package com.rozsa.s3;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.List;

public interface StorageService {
    List<String> listDirectories();

    List<String> listResources(String directory);

    void deleteResource(String resource);

    String createDirectory(String directory);

    String createResource(File file, String contentType, String ext, String directory);

    StorageResourceInputStream getResource(String storageId);

    URL getResourceAccessUrl(String storageId, Date expirationDate);
}
