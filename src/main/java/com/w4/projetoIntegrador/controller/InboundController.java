package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.InboundDto;
import com.w4.projetoIntegrador.service.InboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/inboundorder")
public class InboundController {

    @Autowired
    InboundService inboundService;

    @GetMapping("/{id}")
    public ResponseEntity<InboundDto> getInbound(@PathVariable Long id) {
        return ResponseEntity.ok().body(inboundService.get(id));
    }

    @PostMapping
    public ResponseEntity<InboundDto> cadastra(@Valid @RequestBody InboundDto inbound) {
        return ResponseEntity.status(201).body(inboundService.create(inbound));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InboundDto> putIndound(@Valid @RequestBody InboundDto inbound, @PathVariable Long id) {
        return ResponseEntity.status(201).body(inboundService.update(id, inbound));
    }
}
