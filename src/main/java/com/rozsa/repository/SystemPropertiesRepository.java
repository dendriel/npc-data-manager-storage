package com.rozsa.repository;

import com.rozsa.repository.model.SystemProperties;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemPropertiesRepository extends CrudRepository<SystemProperties, Long> {

    SystemProperties findByKey(String key);
}
