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

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("carId")
    @JoinColumn(name = "car_id", insertable = false, updatable = false)
    private Car car;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("accessoryId")
    @JoinColumn(name = "accessory_id", insertable = false, updatable = false)
    private Accessory accessory;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Embeddable
    public static class CarAccessoryId implements Serializable {
        private Long carId;
        private Long accessoryId;
    }
}
