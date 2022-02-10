package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.SectionDto;
import com.w4.projetoIntegrador.dtos.ValidDueDateProductsDto;
import com.w4.projetoIntegrador.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/sections")
public class SectionController {

    @Autowired
    SectionService sectionService;

    @GetMapping("/{id}")
    public ResponseEntity<SectionDto> get(@PathVariable Long id) {
        return ResponseEntity.ok().body(sectionService.get(id));
    }

    @PostMapping()
    public ResponseEntity<SectionDto> newSection(@Valid @RequestBody SectionDto s) {
        return ResponseEntity.status(201).body(sectionService.save(s));
    }

    @GetMapping("/valid/section/{id}/days/{days}")
    public ResponseEntity<List<ValidDueDateProductsDto>> getValidDueDateProducts(@PathVariable Long id, @PathVariable Integer days) {
        return ResponseEntity.status(200).body(sectionService.getValidDueDateProducts(id, days));
    }

    @GetMapping("/valid/category/{category}/days/{days}")
    public ResponseEntity<List<ValidDueDateProductsDto>> getValidDueDateProductsByCategory(@PathVariable String category, @PathVariable Integer days, @RequestParam String orderBy) {
        return ResponseEntity.status(200).body(sectionService.getValidDueDateProductsByCategory(category, days, orderBy));
    }
}

