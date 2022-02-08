package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.SectionDto;
import com.w4.projetoIntegrador.dtos.ValidDueDateProductsDto;
import com.w4.projetoIntegrador.entities.Section;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.repository.SectionRepository;
import com.w4.projetoIntegrador.service.SectionService;
import com.w4.projetoIntegrador.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import com.w4.projetoIntegrador.repository.SectionRepository.ValidDueDateProducts;

public class SectionTest {

    private Warehouse warehouse = Warehouse.builder().id(1l).name("warehouse").build();
    private Section section = Section.builder().id(1l).warehouse(warehouse).totalSpace(11f).build();
    private SectionDto sectionDto = SectionDto.builder().warehouseId(1l).build();



    private ValidDueDateProductsDto validProduct = ValidDueDateProductsDto.builder()
    .productId(3L)
    .dueDate(LocalDate.now()).productTypeId(2L).build();

    @Test
    public void deveCadastrarUmWarehouse() {

        //arrange
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);

        Mockito.when(mockWarehouseService.getWarehouse(Mockito.any())).thenReturn(warehouse);
        Mockito.when(mockSectionRepository.save(Mockito.any())).thenReturn(section);

        SectionService sectionService = new SectionService(mockSectionRepository,mockWarehouseService);

        //act
        SectionDto s = sectionService.save(sectionDto);

        //assertion
        assertEquals(s.getWarehouseId(), sectionDto.getWarehouseId());
    }


    @Test
    public void deveRetornarUmaSectionPassandoId() {
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);

        Mockito.when(mockSectionRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(section));
        Mockito.when(mockWarehouseService.getWarehouse(Mockito.any())).thenReturn(warehouse);

        SectionService sectionService = new SectionService(mockSectionRepository,mockWarehouseService);

        SectionDto s = sectionService.get(1L);

        assertEquals(s.getId(), section.getId());

    }

    @Test
    public void deveRetornarListaDeProdutosValidos() {
        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);
     
        SectionRepository.ValidDueDateProducts mockSectionValidDueDateInterface = Mockito.mock(SectionRepository.ValidDueDateProducts.class);

        List<ValidDueDateProducts>  validDueDateProducts = new ArrayList<>();

        Mockito.when(mockSectionRepository.findValidDueDateProducts(Mockito.anyLong())).thenReturn(validDueDateProducts);

        SectionService sectionService = new SectionService(mockSectionRepository,mockWarehouseService);

        Mockito.when(mockSectionValidDueDateInterface.getDueDate()).thenReturn(validProduct.getDueDate());
        Mockito.when(mockSectionValidDueDateInterface.getProductId()).thenReturn(validProduct.getProductId());

        List<SectionRepository.ValidDueDateProducts> listA = new ArrayList<>();
        listA.add(mockSectionValidDueDateInterface);

        Mockito.when(mockSectionRepository.findValidDueDateProducts(Mockito.anyLong())).thenReturn(listA);

        List<ValidDueDateProductsDto> validDueDateDtoList = sectionService.getValidDueDateProducts(3L, 10);

        assertEquals(validDueDateDtoList.get(0).getProductId(), listA.get(0).getProductId());

    }

    @Test
    public void deveRetornarListaDeProdutosValidosPorCategoriaASC() {
        SectionRepository mockSectionRepository = Mockito.mock(SectionRepository.class);
        WarehouseService mockWarehouseService = Mockito.mock(WarehouseService.class);

        SectionRepository.ValidDueDateProductsByCategory mockSectionValidDueDateInterface = Mockito.mock(SectionRepository.ValidDueDateProductsByCategory.class);

        List<SectionRepository.ValidDueDateProductsByCategory> validProductsByCategory = new ArrayList<>();

        Mockito.when(mockSectionRepository.findValidDueDateProductsByCategoryAsc(Mockito.anyInt())).thenReturn(validProductsByCategory);

        SectionService sectionService = new SectionService(mockSectionRepository,mockWarehouseService);

        Mockito.when(mockSectionValidDueDateInterface.getDueDate()).thenReturn(validProduct.getDueDate());
        Mockito.when(mockSectionValidDueDateInterface.getProductId()).thenReturn(validProduct.getProductId());
        Mockito.when(mockSectionValidDueDateInterface.getProductTypeId()).thenReturn(validProduct.getProductTypeId());

        List<SectionRepository.ValidDueDateProductsByCategory> listA = new ArrayList<>();

        listA.add(mockSectionValidDueDateInterface);

        Mockito.when(mockSectionRepository.findValidDueDateProductsByCategoryAsc(Mockito.anyInt())).thenReturn(listA);

        List<ValidDueDateProductsDto> validDueDateDtoList = sectionService.getValidDueDateProductsByCategory("congelado", 3,"asc");

        assertEquals(validDueDateDtoList.get(0).getProductTypeId(), listA.get(0).getProductTypeId());


    }

}

