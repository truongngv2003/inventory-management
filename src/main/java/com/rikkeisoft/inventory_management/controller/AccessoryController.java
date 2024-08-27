package com.rikkeisoft.inventory_management.controller;

import com.rikkeisoft.inventory_management.dto.AccessoryDTO;
import com.rikkeisoft.inventory_management.service.AccessoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long accessoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Long> categoryIds,
            @RequestParam(required = false) List<Long> manufacturerIds,
            @RequestParam(required = false) List<Long> carIds,
            @PageableDefault(page = 0, size = 20) Pageable pageable) {

        List<AccessoryDTO> accessoryDTOs = accessoryService.getAccessories(name, description, accessoryId, minPrice, maxPrice, categoryIds, manufacturerIds, carIds, pageable);
        return ResponseEntity.ok(accessoryDTOs);
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