package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.BuyerDto;
import com.w4.projetoIntegrador.service.BuyerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/buyers")
public class BuyerController {

    @Autowired
    BuyerService buyerService;

    @GetMapping("/{id}")
    public ResponseEntity<BuyerDto> getBuyer(@PathVariable Long id){
        return ResponseEntity.ok().body(buyerService.get(id));
    }

    @PostMapping("/")
    public ResponseEntity<BuyerDto> createBuyer(@RequestBody BuyerDto buyerDto){
        return ResponseEntity.status(201).body(buyerService.create(buyerDto));
    }
}
