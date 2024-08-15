package com.rikkeisoft.inventory_management.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManufacturerDTO {
    private Long id;

    @NotBlank(message = "Manufacturer's name cannot be null or blank")
    @Size(max = 50, message = "Manufacturer's name cannot be longer than 50 characters")
    private String name;
}
