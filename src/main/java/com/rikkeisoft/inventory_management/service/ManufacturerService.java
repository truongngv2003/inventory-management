package com.rikkeisoft.inventory_management.service;

import com.rikkeisoft.inventory_management.dto.ManufacturerDTO;
import com.rikkeisoft.inventory_management.exception.NotFoundException;
import com.rikkeisoft.inventory_management.mapper.ManufacturerMapper;
import com.rikkeisoft.inventory_management.model.Manufacturer;
import com.rikkeisoft.inventory_management.repository.ManufacturerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManufacturerService {

    private final ManufacturerRepository manufacturerRepository;


    public ManufacturerDTO getManufacturerById(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found or has been deleted"));

        return ManufacturerMapper.INSTANCE.toManufacturerDTO(manufacturer);
    }


    public List<ManufacturerDTO> getManufacturers() {
        return manufacturerRepository.findAllByIsDeletedFalse().stream()
                .map(ManufacturerMapper.INSTANCE::toManufacturerDTO)
                .toList();
    }


    public ManufacturerDTO createManufacturer(ManufacturerDTO manufacturerDTO) {
        if (manufacturerRepository.existsByName(manufacturerDTO.getName())) {
            throw new DataIntegrityViolationException("Manufacturer with the same name already exists");
        }

        Manufacturer manufacturer = ManufacturerMapper.INSTANCE.toManufacturer(manufacturerDTO);
        manufacturer = manufacturerRepository.save(manufacturer);
        return ManufacturerMapper.INSTANCE.toManufacturerDTO(manufacturer);
    }


    public ManufacturerDTO updateManufacturer(Long id, ManufacturerDTO manufacturerDTO) {

        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found with id = " + id));

        if (!manufacturer.getName().equals(manufacturerDTO.getName()) && manufacturerRepository.existsByName(manufacturerDTO.getName())) {
            throw new DataIntegrityViolationException("Manufacturer with the same name already exists");
        }

        manufacturer.setName(manufacturerDTO.getName());

        manufacturer = manufacturerRepository.save(manufacturer);

        return ManufacturerMapper.INSTANCE.toManufacturerDTO(manufacturer);
    }


    public ManufacturerDTO deleteManufacturer(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Manufacturer not found with id = " + id));

        manufacturer.setIsDeleted(true);
        manufacturer = manufacturerRepository.save(manufacturer);

        return ManufacturerMapper.INSTANCE.toManufacturerDTO(manufacturer);
    }

}
