package com.rikkeisoft.inventory_management.model;

import jakarta.persistence.*;
import java.io.Serializable;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "car_accessory")
public class CarAccessory {

    @EmbeddedId
    private CarAccessoryId id;

    @ManyToOne
    @MapsId("carId")
    @JoinColumn(name = "car_id", insertable = false, updatable = false)
    private Car car;

    @ManyToOne
    @MapsId("accessoryId")
    @JoinColumn(name = "accessory_id", insertable = false, updatable = false)
    private Accessory accessory;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class CarAccessoryId implements Serializable {
        private Long carId;
        private Long accessoryId;
    }
}
