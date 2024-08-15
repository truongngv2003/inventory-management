package com.rikkeisoft.inventory_management.mapper;

import com.rikkeisoft.inventory_management.model.Manufacturer;
import com.rikkeisoft.inventory_management.dto.ManufacturerDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    ManufacturerMapper INSTANCE = Mappers.getMapper(ManufacturerMapper.class);

    ManufacturerDTO toManufacturerDTO(Manufacturer manufacturer);

    Manufacturer toManufacturer(ManufacturerDTO manufacturerDTO);
}
