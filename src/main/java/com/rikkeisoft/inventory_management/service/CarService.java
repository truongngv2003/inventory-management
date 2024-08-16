package com.rikkeisoft.inventory_management.service;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.CarMapper;
import com.rikkeisoft.inventory_management.model.Car;
import com.rikkeisoft.inventory_management.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;


    public CarDTO getCarById(Long id) {
        Car car = carRepository.findById(id)
                .filter(m -> !m.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        return CarMapper.INSTANCE.toCarDTO(car);
    }


    public List<CarDTO> getCars(Pageable pageable) {
        Page<Car> cars = carRepository.findAll(pageable);
        return cars.stream()
                .filter(car -> !car.getIsDeleted())
                .map(CarMapper.INSTANCE::toCarDTO)
                .toList();
    }
}