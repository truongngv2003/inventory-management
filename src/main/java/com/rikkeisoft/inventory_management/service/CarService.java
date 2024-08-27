package com.rikkeisoft.inventory_management.service;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.dto.ManufacturerDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.CarMapper;
import com.rikkeisoft.inventory_management.mapper.ManufacturerMapper;
import com.rikkeisoft.inventory_management.model.Car;
import com.rikkeisoft.inventory_management.model.Manufacturer;
import com.rikkeisoft.inventory_management.repository.CarRepository;
import com.rikkeisoft.inventory_management.repository.ManufacturerRepository;
import com.rikkeisoft.inventory_management.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final ManufacturerRepository manufacturerRepository;

    public CarDTO getCarById(Long id) {
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        return CarMapper.INSTANCE.toCarDTO(car);
    }


    public List<CarDTO> getCars(String name, Long carId, List<Long> manufacturerIds, Date createdFrom, Date createdTo, Pageable pageable) {
        Specification<Car> spec = CarSpecification.filterCars(name, carId, manufacturerIds, createdFrom, createdTo);

        Page<Car> cars = carRepository.findAll(spec, pageable);

        return cars.stream()
                .map(CarMapper.INSTANCE::toCarDTO)
                .toList();
    }


    public CarDTO createCar(Long manufacturerId, CarDTO carDTO) {

        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(manufacturerId)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found or has been deleted"));

        if (carRepository.existsByNameAndManufacturerAndIsDeletedFalse(carDTO.getName(), manufacturer)) {
            throw new DataIntegrityViolationException("Car with the same name already exists in this manufacturer");
        }

        ManufacturerDTO manufacturerDTO = ManufacturerMapper.INSTANCE.toManufacturerDTO(manufacturer);
        carDTO.setManufacturerDTO(manufacturerDTO);

        Car car = CarMapper.INSTANCE.toCar(carDTO);
        car = carRepository.save(car);

        return CarMapper.INSTANCE.toCarDTO(car);
    }


    public CarDTO updateCar(Long carId, CarDTO carDTO){

        Car car = carRepository.findByIdAndIsDeletedFalse(carId)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        if (!car.getName().equals(carDTO.getName()) && carRepository.existsByNameAndManufacturerAndIsDeletedFalse(carDTO.getName(), car.getManufacturer())){
            throw new DataIntegrityViolationException("Car with the same name already exist in this manufacturer");
        }

        car.setName(carDTO.getName());

        car = carRepository.save(car);

        return CarMapper.INSTANCE.toCarDTO(car);
    }

    public CarDTO deleteCar(Long id){
        Car car = carRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        if (carRepository.existsActiveCarAccessories(id)) {
            throw new DataIntegrityViolationException("Car cannot be deleted as it contains one or more active accessories.");
        }

        car.setIsDeleted(true);
        car = carRepository.save(car);

        return CarMapper.INSTANCE.toCarDTO(car);
    }
}