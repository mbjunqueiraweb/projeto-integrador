package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.ProductAnnouncementDto;
import com.w4.projetoIntegrador.service.ProductAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/productannouncements")
public class ProductAnnouncementController {

    @Autowired
    ProductAnnouncementService productAnnouncementService;

    @GetMapping("/{id}")
    public ResponseEntity<ProductAnnouncementDto> getProduct(@PathVariable Long id){
        return ResponseEntity.ok().body(productAnnouncementService.get(id));
    }

    @PostMapping()
    public ResponseEntity<ProductAnnouncementDto> newProduct(@Valid @RequestBody ProductAnnouncementDto p){
        return ResponseEntity.status(201).body(productAnnouncementService.save(p));
    }
}
