package com.rozsa.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;

import java.io.InputStream;

@Getter
@AllArgsConstructor
public class StorageResourceInputStream {
    private final InputStream inputStream;
    private final String contentType;
    private final Long contentLength;

    public MediaType getMediaType() {
        return MediaType.parseMediaType(contentType);
    }
}
