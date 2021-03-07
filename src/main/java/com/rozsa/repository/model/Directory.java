package com.rozsa.repository.model;

import javax.persistence.*;

@Entity
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String storageId;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String idStorage) {
        this.storageId = idStorage;
    }
}
