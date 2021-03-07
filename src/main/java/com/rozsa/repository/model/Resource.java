package com.rozsa.repository.model;

import com.rozsa.repository.ResourceType;

import javax.persistence.*;

@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private ResourceType type;

    @Column(nullable = false)
    private String storageId;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(nullable=false)
    private Directory directory;

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

    public ResourceType getType() {
        return type;
    }

    public void setType(ResourceType type) {
        this.type = type;
    }

    public String getStorageId() {
        return storageId;
    }

    public void setStorageId(String idStorage) {
        this.storageId = idStorage;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }
}
