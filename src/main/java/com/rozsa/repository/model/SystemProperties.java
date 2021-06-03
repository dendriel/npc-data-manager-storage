package com.rozsa.repository.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class SystemProperties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "config", unique = true)
    private String key;

    @Column(name = "val")
    private String value;
}
