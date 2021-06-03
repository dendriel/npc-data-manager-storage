package com.rozsa.repository.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String storageId;

    @Transient
    private Long resourcesCount;
}
