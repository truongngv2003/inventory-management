package com.rikkeisoft.inventory_management.dto;

import com.rikkeisoft.inventory_management.model.Manufacturer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    private Long id;

    @NotBlank(message = "Car's name cannot be null or blank")
    @Size(max = 30, message = "Car's name cannot be longer than 30 characters")
    private String name;
    private Date creationDate;
    private Date updateDate;
    private ManufacturerDTO manufacturerDTO;
}