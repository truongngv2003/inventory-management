package com.rikkeisoft.inventory_management.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.model.Car;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(source = "manufacturer", target = "manufacturerDTO")
    CarDTO toCarDTO(Car car);

    @Mapping(source = "manufacturerDTO", target = "manufacturer")
    Car toCar(CarDTO carDTO);
}

