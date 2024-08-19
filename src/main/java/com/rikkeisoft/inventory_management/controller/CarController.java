package com.rikkeisoft.inventory_management.controller;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping()
    public ResponseEntity<List<CarDTO>> getCars(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(carService.getCars(pageable));
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }


    @PostMapping("/{manufacturerId}")
    public ResponseEntity<CarDTO> createCar(@PathVariable Long manufacturerId, @RequestBody @Valid CarDTO carDTO){
        return ResponseEntity.ok(carService.createCar(manufacturerId, carDTO));
    }


    @PutMapping("/{manufacturerId}/{carId}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long manufacturerId, @PathVariable Long carId, @RequestBody @Valid CarDTO carDTO){
        return ResponseEntity.ok(carService.updateCar(manufacturerId, carId, carDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CarDTO> deleteCar(@PathVariable Long id){
        return ResponseEntity.ok(carService.deleteCar(id));
    }
}