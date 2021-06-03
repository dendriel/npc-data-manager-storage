package com.rozsa.business.impl;

import com.rozsa.business.SystemPropertiesBusiness;
import com.rozsa.repository.SystemPropertiesRepository;
import com.rozsa.repository.model.SystemProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class SystemPropertiesBusinessImpl implements SystemPropertiesBusiness {
    private static final String bucketKey = "storage.bucket.name";

    private final SystemPropertiesRepository systemPropertiesRepository;

    public String getStorageBucketName() {
        SystemProperties result = systemPropertiesRepository.findByKey(bucketKey);
        return result != null ? result.getValue() : "npcdatamanager";
    }
}
