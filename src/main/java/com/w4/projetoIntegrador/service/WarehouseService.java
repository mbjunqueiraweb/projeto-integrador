package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.dtos.ProductsByWarehouseDto;
import com.w4.projetoIntegrador.dtos.WarehouseDto;
import com.w4.projetoIntegrador.dtos.WarehouseStockDto;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WarehouseService {

    WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse getWarehouse(Long id) {
        try {
            return warehouseRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new NotFoundException("Warehouse " + id + " não encontrada na base de dados.");
        }
    }

    public WarehouseDto get(Long id) {
        return WarehouseDto.convert(getWarehouse(id));
    }

    public WarehouseDto save(Warehouse wh) {
        return WarehouseDto.convert(warehouseRepository.save(wh));
    }

    public ProductsByWarehouseDto getWarehouseStock(Long id) {
        List<WarehouseRepository.ProductWarehouse> list = warehouseRepository.getStockByWarehouse(id);
        if (list.size() == 0) throw new NotFoundException("Não encontrado produto com id " + id);
        ProductsByWarehouseDto pto = ProductsByWarehouseDto.builder().productId(id)
                .build();
        List<WarehouseStockDto> wd = new ArrayList<>();
        for (WarehouseRepository.ProductWarehouse item : list) {
            WarehouseStockDto ws = WarehouseStockDto.builder()
                    .warehosecode(item.getWarehouse())
                    .totalquantity(item.getStock())
                    .section(item.getSection())
                    .batch(item.getBatch())
                    .build();
            wd.add(ws);
        }
        pto.setWarehouses(wd);
        return pto;
    }
}

