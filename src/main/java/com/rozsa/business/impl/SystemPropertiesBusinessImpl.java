package com.rozsa.business.impl;

import com.rozsa.business.SystemPropertiesBusiness;
import com.rozsa.repository.SystemPropertiesRepository;
import com.rozsa.repository.model.SystemProperties;
import org.springframework.stereotype.Service;

@Service
public class SystemPropertiesBusinessImpl implements SystemPropertiesBusiness {
    private static final String bucketKey = "storage.bucket.name";

    private final SystemPropertiesRepository systemPropertiesRepository;

    public SystemPropertiesBusinessImpl(SystemPropertiesRepository systemPropertiesRepository) {
        this.systemPropertiesRepository = systemPropertiesRepository;
    }

    public String getStorageBucketName() {
        SystemProperties result = systemPropertiesRepository.findByKey(bucketKey);
        return result != null ? result.getValue() : "npcdatamanager";
    }


}
