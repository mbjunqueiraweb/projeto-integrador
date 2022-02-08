package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.dtos.ProductsByWarehouseDto;
import com.w4.projetoIntegrador.dtos.WarehouseDto;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.repository.WarehouseRepository;
import com.w4.projetoIntegrador.service.WarehouseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {


    private Warehouse warehouse = Warehouse.builder().id(1l).name("warehouse").build();

    private WarehouseDto warehouseDto = WarehouseDto.builder().name("warehouse").build();

    @Test
    public void deveCadastrarUmWarehouse() {

        //arrange
        WarehouseRepository mock = Mockito.mock(WarehouseRepository.class);
        Mockito.when(mock.save(Mockito.any())).thenReturn(warehouse);
        WarehouseService warehouseService = new WarehouseService(mock);

        //act
        WarehouseDto w = warehouseService.save(warehouse);

        //assertion
        assertEquals(w.getName(), warehouse.getName());
    }

    @Test
    public void deveBuscarUmWarehouse() {

        //arrange
        WarehouseRepository mock = Mockito.mock(WarehouseRepository.class);
        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.of(warehouse));
        WarehouseService warehouseService = new WarehouseService(mock);

        //act
        WarehouseDto w = warehouseService.get(1L);

        //assertion
        assertEquals(w.getName(), warehouse.getName());
    }


    @Test
    public void deveBuscarStockDeProductNaWarehouse() {

        //arrange
        WarehouseRepository mock = Mockito.mock(WarehouseRepository.class);
        Mockito.when(mock.findById(Mockito.anyLong())).thenReturn(Optional.of(warehouse));
        WarehouseService warehouseService = new WarehouseService(mock);

        WarehouseRepository.ProductWarehouse mockStockByWareHouse = Mockito.mock(WarehouseRepository.ProductWarehouse.class);

        Mockito.when(mockStockByWareHouse.getWarehouse()).thenReturn(1L);
        Mockito.when(mockStockByWareHouse.getStock()).thenReturn(10);

        List<WarehouseRepository.ProductWarehouse> pwList = new ArrayList<>();
        pwList.add(mockStockByWareHouse);
        Mockito.when(mock.getStockByWarehouse(Mockito.anyLong())).thenReturn(pwList);

        //act
        ProductsByWarehouseDto pw = warehouseService.getWarehouseStock(1L);

        //assertion
        assertTrue(pw.getWarehouses().get(0).getWarehosecode().equals(1L));

    }
}

