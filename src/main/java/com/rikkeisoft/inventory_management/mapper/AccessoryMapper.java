package com.rikkeisoft.inventory_management.mapper;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.model.CarAccessory;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import com.rikkeisoft.inventory_management.dto.AccessoryDTO;
import com.rikkeisoft.inventory_management.model.Accessory;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    AccessoryMapper INSTANCE = Mappers.getMapper(AccessoryMapper.class);

    @Mapping(source = "manufacturer", target = "manufacturerDTO")
    @Mapping(source = "category", target = "categoryDTO")
    @Mapping(source = "carAccessories", target = "cars")
    AccessoryDTO toAccessoryDTO(Accessory accessory);

    @Mapping(source = "manufacturerDTO", target = "manufacturer")
    @Mapping(source = "categoryDTO", target = "category")
    @Mapping(target = "carAccessories", ignore = true)
    Accessory toAccessory(AccessoryDTO accessoryDTO);

    //    default Long map(CarAccessory.CarAccessoryId value) {
    //        return value != null ? value.getCarId() : null;
    //    }
    default Set<CarDTO> map(Set<CarAccessory> carAccessories) {
        return carAccessories.stream()
            .map(CarAccessory::getCar)
            .map(CarMapper.INSTANCE::toCarDTO)
            .collect(Collectors.toSet());
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Accessory updateAccessoryFromDTO(AccessoryDTO accessoryDTO, @MappingTarget Accessory accessory);
}
