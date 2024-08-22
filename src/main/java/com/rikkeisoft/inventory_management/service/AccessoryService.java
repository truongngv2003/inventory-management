package com.rikkeisoft.inventory_management.service;

import com.cloudinary.Cloudinary;
import com.rikkeisoft.inventory_management.dto.AccessoryDTO;
import com.rikkeisoft.inventory_management.dto.CategoryDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.AccessoryMapper;
import com.rikkeisoft.inventory_management.model.*;
import com.rikkeisoft.inventory_management.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import com.cloudinary.utils.ObjectUtils;

import java.util.HashSet;
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


    public List<AccessoryDTO> getAccessories(Pageable pageable) {

        return accessoryRepository.findByIsDeletedFalse(pageable).stream()
                .map(AccessoryMapper.INSTANCE::toAccessoryDTO)
                .toList();
    }


    public AccessoryDTO createAccessory(Long carId, Long manufacturerId, AccessoryDTO accessoryDTO, MultipartFile[] files) {
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

        if (accessoryRepository.existsByAccessoryNameAndCarAndManufacturer(accessoryDTO.getName(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same name already exists for this car and manufacturer.");
        }

        if (accessoryRepository.existsByAccessoryCodeAndCarAndManufacturer(accessoryDTO.getCode(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same code already exists for this car and manufacturer.");
        }

        Accessory accessory = AccessoryMapper.INSTANCE.toAccessory(accessoryDTO);
        accessory.setManufacturer(manufacturer);

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

//        if (accessory.getCarAccessories() == null) {
//            accessory.setCarAccessories(new HashSet<>());
//        }

        accessory.getCarAccessories().add(carAccessory);
        accessory = accessoryRepository.save(accessory);


        return AccessoryMapper.INSTANCE.toAccessoryDTO(accessory);
    }


    public AccessoryDTO updateAccessory(Long accessoryId, Long carId, Long manufacturerId, AccessoryDTO accessoryDTO) {
        // Tìm kiếm Accessory cần cập nhật
        Accessory existingAccessory = accessoryRepository.findByIdAndIsDeletedFalse(accessoryId)
                .orElseThrow(() -> new NotFoundException("Accessory not found or has been deleted"));

        // Kiểm tra Manufacturer
        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(manufacturerId)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found or has been deleted"));

        // Kiểm tra Car
        Car car = carRepository.findByIdAndIsDeletedFalse(carId)
                .orElseThrow(() -> new NotFoundException("Car not found or has been deleted"));

        // Kiểm tra Category
        CategoryDTO categoryDTO = accessoryDTO.getCategoryDTO();
        if (!categoryRepository.existsByIdAndIsDeletedFalse(categoryDTO.getId())) {
            throw new NotFoundException("Category not found or has been deleted");
        }

        // Kiểm tra xem Car có thuộc Manufacturer không
        if (!manufacturer.equals(car.getManufacturer())) {
            throw new IllegalArgumentException("The selected car does not belong to the specified manufacturer.");
        }

        // Kiểm tra tên và mã code có trùng lặp với những Accessory khác trong cùng Car và Manufacturer không
        if (!existingAccessory.getName().equals(accessoryDTO.getName()) &&
                accessoryRepository.existsByAccessoryNameAndCarAndManufacturer(accessoryDTO.getName(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same name already exists for this car and manufacturer.");
        }

        if (!existingAccessory.getCode().equals(accessoryDTO.getCode()) &&
                accessoryRepository.existsByAccessoryCodeAndCarAndManufacturer(accessoryDTO.getCode(), car, manufacturer)) {
            throw new DataIntegrityViolationException("An accessory with the same code already exists for this car and manufacturer.");
        }

        // Cập nhật thông tin Accessory từ DTO
        existingAccessory = AccessoryMapper.INSTANCE.updateAccessoryFromDTO(accessoryDTO, existingAccessory);
        //existingAccessory.setManufacturer(manufacturer);

        // Xử lý upload file lên Cloudinary
//        if (files != null && files.length > 0) {
//            boolean hasValidFile = false;
//            for (MultipartFile file : files) {
//                if (!file.isEmpty()) {
//                    hasValidFile = true;
//                    break;
//                }
//            }
//
//            if (!hasValidFile) {
//                throw new NotFoundException("At least one valid file must be selected!");
//            }
//
//            for (MultipartFile file : files) {
//                if (!file.isEmpty()) {
//                    try {
//                        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
//                        String imageUrl = uploadResult.get("url").toString();
//
//                        Attachment attachment = new Attachment();
//                        attachment.setSource(imageUrl);
//                        attachment.setExtension(file.getContentType());
//                        attachment.setName(file.getOriginalFilename());
//                        attachment.setAccessory(existingAccessory);
//
//                        existingAccessory.getAttachments().add(attachment);
//                    } catch (IOException e) {
//                        throw new RuntimeException("Failed to upload file: " + file.getOriginalFilename(), e);
//                    }
//                }
//            }
//        }

        // Cập nhật CarAccessory nếu cần thiết
//        CarAccessory existingCarAccessory = carAccessoryRepository.findByCarAndAccessory(car, existingAccessory)
//                .orElse(new CarAccessory(new CarAccessory.CarAccessoryId(car.getId(), existingAccessory.getId()), car, existingAccessory));
//
//        existingCarAccessory.setCar(car);
//        existingAccessory.getCarAccessories().add(existingCarAccessory);

        // Lưu Accessory đã cập nhật
        existingAccessory = accessoryRepository.save(existingAccessory);

        return AccessoryMapper.INSTANCE.toAccessoryDTO(existingAccessory);
    }


    //Chưa xóa các quan hệ
    public AccessoryDTO deleteAccessory(Long accessoryId) {
        Accessory accessory = accessoryRepository.findByIdAndIsDeletedFalse(accessoryId)
                .orElseThrow(() -> new NotFoundException("Accessory not found or has been deleted"));

        accessory.setIsDeleted(true);
        accessoryRepository.save(accessory);

        return AccessoryMapper.INSTANCE.toAccessoryDTO(accessory);
    }
}