package com.rikkeisoft.inventory_management.dto;

import com.rikkeisoft.inventory_management.model.Accessory;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private Long id;

    private String source;

    private String extension;

    private String name;

}
