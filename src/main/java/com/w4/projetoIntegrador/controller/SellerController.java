package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.SellerDto;
import com.w4.projetoIntegrador.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sellers")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @GetMapping("/{id}")
    public ResponseEntity<SellerDto> getSeller(@PathVariable Long id) {
        return ResponseEntity.ok().body(sellerService.get(id));
    }

    @PostMapping()
    public ResponseEntity<SellerDto> newSeller(@Valid @RequestBody SellerDto s) {
        return ResponseEntity.status(201).body(sellerService.save(s));
    }
}

