package com.rikkeisoft.inventory_management.service;

import com.cloudinary.Cloudinary;
import com.rikkeisoft.inventory_management.dto.AccessoryDTO;
import com.rikkeisoft.inventory_management.dto.CategoryDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.AccessoryMapper;
import com.rikkeisoft.inventory_management.model.*;
import com.rikkeisoft.inventory_management.repository.*;
import com.rikkeisoft.inventory_management.specification.AccessorySpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.cloudinary.utils.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AccessoryService {

    private final ManufacturerRepository manufacturerRepository;
    private final CarRepository carRepository;
    private final AccessoryRepository accessoryRepository;
    private final CategoryRepository categoryRepository;
    private final Cloudinary cloudinary;


    public AccessoryDTO getAccessoryById(Long id) {

        Accessory accessory = accessoryRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Accessory not found or has been deleted"));

        return AccessoryMapper.INSTANCE.toAccessoryDTO(accessory);
    }


    public List<AccessoryDTO> getAccessories(String name, String description, Long accessoryId,
                                             BigDecimal minPrice, BigDecimal maxPrice, List<Long> categoryIds,
                                             List<Long> manufacturerIds, List<Long> carIds, Pageable pageable) {

        Specification<Accessory> spec = AccessorySpecification.filterAccessories(name, description, accessoryId, minPrice, maxPrice, categoryIds, manufacturerIds, carIds);

        Page<Accessory> accessories = accessoryRepository.findAll(spec, pageable);

        return accessories.stream()
                .map(AccessoryMapper.INSTANCE::toAccessoryDTO)
                .toList();
    }


    public AccessoryDTO createAccessory(Long carId, Long manufacturerId, AccessoryDTO accessoryDTO, MultipartFile[] files) {
        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(manufacturerId)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found or has been deleted"));

        Car car = carRepository.findByIdAndIsDeletedFalse(carId)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        CategoryDTO categoryDTO = accessoryDTO.getCategoryDTO();
        Category category = categoryRepository.findByIdAndIsDeletedFalse(categoryDTO.getId())
                .orElseThrow(() -> new NotFoundException("Category not found or has been deleted"));

        if (!manufacturer.equals(car.getManufacturer())) {
            throw new IllegalArgumentException("The selected car does not belong to the specified manufacturer.");
        }

        if (accessoryRepository.existsByAccessoryNameAndCarAndManufacturer(accessoryDTO.getName(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same name already exists for this car and manufacturer.");
        }

        if (accessoryRepository.existsByAccessoryCodeAndCarAndManufacturer(accessoryDTO.getCode(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same code already exists for this car and manufacturer.");
        }

        Accessory accessory = AccessoryMapper.INSTANCE.toAccessory(accessoryDTO);
        accessory.setManufacturer(manufacturer);
        accessory.setCategory(category);


        //Xu ly upload file len Cloudiary
        if (files == null || files.length == 0) {
            throw new NotFoundException("At least one file must be selected!");
        }


        boolean hasValidFile = false;
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                hasValidFile = true;
                break;
            }
        }

        if (!hasValidFile) {
            throw new NotFoundException("At least one valid file must be selected!");
        }

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = uploadResult.get("url").toString();

                    Attachment attachment = new Attachment();
                    attachment.setSource(imageUrl);
                    attachment.setExtension(file.getContentType());
                    attachment.setName(file.getOriginalFilename());
                    attachment.setAccessory(accessory);

                    accessory.getAttachments().add(attachment);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
                }
            }
        }


        //------------------------------------------------------
        CarAccessory.CarAccessoryId carAccessoryId = new CarAccessory.CarAccessoryId();
        carAccessoryId.setCarId(car.getId());
        carAccessoryId.setAccessoryId(accessory.getId());

        CarAccessory carAccessory = new CarAccessory();
        carAccessory.setId(carAccessoryId);
        carAccessory.setCar(car);
        carAccessory.setAccessory(accessory);

        accessory.getCarAccessories().add(carAccessory);
        accessory = accessoryRepository.save(accessory);


        return AccessoryMapper.INSTANCE.toAccessoryDTO(accessory);
    }


    public AccessoryDTO updateAccessory(Long accessoryId, Long carId, Long manufacturerId, AccessoryDTO accessoryDTO) {
        Accessory existingAccessory = accessoryRepository.findByIdAndIsDeletedFalse(accessoryId)
                .orElseThrow(() -> new NotFoundException("Accessory not found or has been deleted"));

        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(manufacturerId)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found or has been deleted"));

        Car car = carRepository.findByIdAndIsDeletedFalse(carId)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        CategoryDTO categoryDTO = accessoryDTO.getCategoryDTO();
        if (!categoryRepository.existsByIdAndIsDeletedFalse(categoryDTO.getId())) {
            throw new NotFoundException("Category not found or has been deleted");
        }

        if (!manufacturer.equals(car.getManufacturer())) {
            throw new IllegalArgumentException("The selected car does not belong to the specified manufacturer.");
        }

        if (!existingAccessory.getName().equals(accessoryDTO.getName()) &&
                accessoryRepository.existsByAccessoryNameAndCarAndManufacturer(accessoryDTO.getName(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same name already exists for this car and manufacturer.");
        }

        if (!existingAccessory.getCode().equals(accessoryDTO.getCode()) &&
                accessoryRepository.existsByAccessoryCodeAndCarAndManufacturer(accessoryDTO.getCode(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same code already exists for this car and manufacturer.");
        }

        // Cập nhật thông tin Accessory từ DTO
        existingAccessory.setManufacturer(manufacturer);
        existingAccessory = AccessoryMapper.INSTANCE.updateAccessoryFromDTO(accessoryDTO, existingAccessory);


//        CarAccessory existingCarAccessory = carAccessoryRepository.findByCarAndAccessory(car, existingAccessory)
//                .orElse(new CarAccessory(new CarAccessory.CarAccessoryId(car.getId(), existingAccessory.getId()), car, existingAccessory));
//
//        existingCarAccessory.setCar(car);
//        existingAccessory.getCarAccessories().add(existingCarAccessory);

        // Lưu Accessory
        existingAccessory = accessoryRepository.save(existingAccessory);

        return AccessoryMapper.INSTANCE.toAccessoryDTO(existingAccessory);
    }


    public AccessoryDTO deleteAccessory(Long accessoryId) {
        Accessory accessory = accessoryRepository.findByIdAndIsDeletedFalse(accessoryId)
                .orElseThrow(() -> new NotFoundException("Accessory not found or has been deleted"));

        accessory.setIsDeleted(true);
        accessory.getAttachments().forEach(attachment -> attachment.setIsDeleted(true));
        accessory.getCarAccessories().forEach(carAccessory -> carAccessory.setIsDeleted(true));

        accessoryRepository.save(accessory);

        return AccessoryMapper.INSTANCE.toAccessoryDTO(accessory);
    }
}