package com.w4.projetoIntegrador.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.w4.projetoIntegrador.dtos.SectionDto;
import com.w4.projetoIntegrador.dtos.ValidDueDateProductsDto;
import com.w4.projetoIntegrador.entities.Section;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.BusinessException;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.SectionRepository;
import com.w4.projetoIntegrador.repository.SectionRepository.ValidDueDateProducts;
import com.w4.projetoIntegrador.repository.SectionRepository.ValidDueDateProductsByCategory;

import org.springframework.stereotype.Service;

@Service
public class SectionService {

    SectionRepository sectionRepository;

    WarehouseService warehouseService;

    public SectionService(SectionRepository sectionRepository, WarehouseService warehouseService) {
        this.sectionRepository = sectionRepository;
        this.warehouseService = warehouseService;
    }

    public Section getSection(Long id) {
        try {
            Section section = sectionRepository.findById(id).orElse(null);
            return section;
        } catch (RuntimeException e) {
            throw new NotFoundException("Section " + id + " não encontrada na base de dados.");
        }
    }

    public SectionDto get(Long id) {
        return SectionDto.convert(getSection(id));
    }

    public SectionDto save(SectionDto sectionDto) {
        Warehouse w = warehouseService.getWarehouse(sectionDto.getWarehouseId());
        Section section = SectionDto.convert(sectionDto, w);
        sectionRepository.save(section);
        return sectionDto;
    }

    public List<ValidDueDateProductsDto> getValidDueDateProducts(Long id, Integer dias) {
           List<SectionRepository.ValidDueDateProducts> validProducts = sectionRepository.findValidDueDateProducts(id);
           if(validProducts.size() == 0) throw  new NotFoundException("Não encontrado setor com este id");

           System.out.println(validProducts.size());
           List<ValidDueDateProductsDto> validDueDateList = new ArrayList<ValidDueDateProductsDto>();
           for (ValidDueDateProducts validDueDateProducts : validProducts) {
               if (LocalDate.now().plusDays(dias).isAfter(validDueDateProducts.getDueDate())) {
                   ValidDueDateProductsDto validDueDateProductsDto = ValidDueDateProductsDto.builder()
                           .batchNumber(validDueDateProducts.getBatchNumber())
                           .productId(validDueDateProducts.getProductId())
                           .productTypeId(validDueDateProducts.getProductTypeId())
                           .dueDate(validDueDateProducts.getDueDate())
                           .quantity(validDueDateProducts.getQuantity())
                           .build();

                   validDueDateList.add(validDueDateProductsDto);
               }
           }
           return validDueDateList;
    }

    public List<ValidDueDateProductsDto> getValidDueDateProductsByCategory(String type, Integer days, String orderBy) {

        Integer id = ProductTypes.valueOf(type).ordinal();
        List<SectionRepository.ValidDueDateProductsByCategory> validProductsByCategory;

        switch (orderBy) {
            case "asc":
                validProductsByCategory = sectionRepository.findValidDueDateProductsByCategoryAsc(id);
                break;

            case "desc":
                validProductsByCategory = sectionRepository.findValidDueDateProductsByCategoryDesc(id);
                break;

            default:
                throw new BusinessException("criterio deve ser asc ou desc");
        }

        List<ValidDueDateProductsDto> validDueDateListByCategory = new ArrayList<ValidDueDateProductsDto>();
        for (ValidDueDateProductsByCategory validDueDateProductsByCategory : validProductsByCategory) {
            if (LocalDate.now().plusDays(days).isAfter(validDueDateProductsByCategory.getDueDate())) {
                ValidDueDateProductsDto validDueDateProductsDto = ValidDueDateProductsDto.builder()
                        .batchNumber(validDueDateProductsByCategory.getBatchNumber())
                        .productId(validDueDateProductsByCategory.getProductId())
                        .productTypeId(validDueDateProductsByCategory.getProductTypeId())
                        .dueDate(validDueDateProductsByCategory.getDueDate())
                        .quantity(validDueDateProductsByCategory.getQuantity())
                        .build();

                validDueDateListByCategory.add(validDueDateProductsDto);
            }
        }
        return validDueDateListByCategory;
    }
}

