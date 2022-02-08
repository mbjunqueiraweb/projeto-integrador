package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.ProductsByWarehouseDto;
import com.w4.projetoIntegrador.dtos.WarehouseDto;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {

    @Autowired
    WarehouseService warehouseService;

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok().body(warehouseService.get(id));
    }

    @PostMapping()
    public ResponseEntity<WarehouseDto> newWarehouse(@Valid @RequestBody Warehouse wh) {
        return ResponseEntity.status(201).body(warehouseService.save(wh));
    }

    @GetMapping("/byproducts/{id}")
    public ResponseEntity<ProductsByWarehouseDto> getWarehouseByProduct(@PathVariable Long id) {
        return ResponseEntity.ok().body(warehouseService.getWarehouseStock(id));
    }
}

