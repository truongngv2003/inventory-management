package com.rikkeisoft.inventory_management.controller;

import com.rikkeisoft.inventory_management.dto.CarDTO;
import com.rikkeisoft.inventory_management.service.CarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;


    @GetMapping()
    public ResponseEntity<List<CarDTO>> getCars(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long carId,
            @RequestParam(required = false) List<Long> manufacturerIds,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date createdTo,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        List<CarDTO> carDTOs = carService.getCars(name, carId, manufacturerIds, createdFrom, createdTo, pageable);
        return ResponseEntity.ok(carDTOs);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
        return ResponseEntity.ok(carService.getCarById(id));
    }


    @PostMapping()
    public ResponseEntity<CarDTO> createCar(@RequestParam Long manufacturerId, @RequestBody @Valid CarDTO carDTO){
        return ResponseEntity.ok(carService.createCar(manufacturerId, carDTO));
    }


    @PutMapping("/{carId}")
    public ResponseEntity<CarDTO> updateCar(@PathVariable Long carId, @RequestBody @Valid CarDTO carDTO){
        return ResponseEntity.ok(carService.updateCar(carId, carDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<CarDTO> deleteCar(@PathVariable Long id){
        return ResponseEntity.ok(carService.deleteCar(id));
    }
}