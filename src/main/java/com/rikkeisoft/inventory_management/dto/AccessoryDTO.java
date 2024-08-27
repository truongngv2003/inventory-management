package com.rikkeisoft.inventory_management.dto;

import com.rikkeisoft.inventory_management.model.Attachment;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessoryDTO {
    private Long id;

    @NotBlank(message = "Accessory's code cannot be null or blank")
    @Size(max = 30, message = "Accessory's code cannot be longer than 30 characters")
    private String code;

    @NotBlank(message = "Accessory's name cannot be null or blank")
    @Size(max = 30, message = "Accessory's name cannot be longer than 30 characters")
    private String name;

    private String description;

    @NotNull(message = "Accessory's price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Accessory's price must be greater than 0")
    private BigDecimal price;

    private ManufacturerDTO manufacturerDTO;

    private Set<CarDTO> Cars;

    private CategoryDTO categoryDTO;

    private Set<AttachmentDTO> attachments;
}