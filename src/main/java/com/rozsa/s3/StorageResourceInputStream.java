package com.rozsa.s3;

import org.springframework.http.MediaType;

import java.io.InputStream;

public class StorageResourceInputStream {
    private String storageId;

    private InputStream inputStream;

    private String contentType;

    private Long contentLength;

    public StorageResourceInputStream(String storageId, InputStream inputStream, String contentType, Long contentLength) {
        this.inputStream = inputStream;
        this.contentType = contentType;
        this.contentLength = contentLength;
    }

    public String storageId() {
        return storageId;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getContentType() {
        return contentType;
    }

    public Long getContentLength() {
        return contentLength;
    }

    public MediaType getMediaType() {
        return MediaType.parseMediaType(contentType);
    }
}
