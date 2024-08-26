package com.rikkeisoft.inventory_management.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source", nullable = false)
    private String source;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @ManyToOne
    @JoinColumn(name = "accessory_id", nullable = false)
    @ToString.Exclude
    private Accessory accessory;
}
