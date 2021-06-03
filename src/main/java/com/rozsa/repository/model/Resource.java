package com.rozsa.repository.model;

import com.rozsa.repository.ResourceType;
import lombok.Data;

import javax.persistence.*;

@Data
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
}
