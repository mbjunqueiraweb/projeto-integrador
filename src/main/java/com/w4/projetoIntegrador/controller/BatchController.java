package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.BatchDto;
import com.w4.projetoIntegrador.service.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batches")
public class BatchController {

    @Autowired
    private BatchService batchService;
    
    @GetMapping("/{id}")
    public ResponseEntity<BatchDto> getBatch(@PathVariable Long id){
        return ResponseEntity.ok().body(batchService.get(id));
    }
}
