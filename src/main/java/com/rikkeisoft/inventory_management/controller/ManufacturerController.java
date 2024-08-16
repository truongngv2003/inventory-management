package com.rikkeisoft.inventory_management.controller;

import com.rikkeisoft.inventory_management.dto.ManufacturerDTO;
import com.rikkeisoft.inventory_management.service.ManufacturerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manufacturers")
@RequiredArgsConstructor
public class ManufacturerController {

    private final ManufacturerService manufacturerService;

    @GetMapping
    public ResponseEntity<List<ManufacturerDTO>> getManufacturers() {
        return ResponseEntity.ok(manufacturerService.getManufacturers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> getManufacturerById(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturerService.getManufacturerById(id));
    }

    @PostMapping
    public ResponseEntity<ManufacturerDTO> createManufacturer(@RequestBody @Valid ManufacturerDTO manufacturerDTO) {
        return ResponseEntity.ok(manufacturerService.createManufacturer(manufacturerDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> updateManufacturer(@PathVariable Long id, @RequestBody @Valid ManufacturerDTO manufacturerDTO) {
        return ResponseEntity.ok(manufacturerService.updateManufacturer(id, manufacturerDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ManufacturerDTO> deleteManufacturer(@PathVariable Long id) {
        return ResponseEntity.ok(manufacturerService.deleteManufacturer(id));
    }
}
