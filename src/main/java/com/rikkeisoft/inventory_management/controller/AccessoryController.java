package com.rikkeisoft.inventory_management.controller;

import com.rikkeisoft.inventory_management.dto.AccessoryDTO;
import com.rikkeisoft.inventory_management.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accessories")
public class AccessoryController {


    private final AccessoryService accessoryService;

    @GetMapping("/{id}")
    public ResponseEntity<AccessoryDTO> getAccessory(@PathVariable Long id) {
        return ResponseEntity.ok(accessoryService.getAccessoryById(id));
    }


    @GetMapping
    public ResponseEntity<List<AccessoryDTO>> getAccessories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(accessoryService.getAccessories(pageable));
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<AccessoryDTO> createAccessory(
            @RequestParam("carId") Long carId,
            @RequestParam("manufacturerId") Long manufacturerId,
            @RequestPart("files") @Valid MultipartFile[] files,
            @RequestPart("accessoryDTO") @Valid AccessoryDTO accessoryDTO) {
        return ResponseEntity.ok(accessoryService.createAccessory(carId, manufacturerId, accessoryDTO, files));
    }



    @PutMapping("/{id}")
    public ResponseEntity<AccessoryDTO> createAccessory(
            @PathVariable("id") Long accessoryId,
            @RequestParam("carId") Long carId,
            @RequestParam("manufacturerId") Long manufacturerId,
            @RequestPart("accessoryDTO") @Valid AccessoryDTO accessoryDTO){
        return ResponseEntity.ok(accessoryService.updateAccessory(accessoryId, carId, manufacturerId, accessoryDTO));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<AccessoryDTO> deleteAccessory(@PathVariable Long id) {
        return ResponseEntity.ok(accessoryService.deleteAccessory(id));
    }
}