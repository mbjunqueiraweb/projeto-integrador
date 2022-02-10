package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.ScheduledCartDto;
import com.w4.projetoIntegrador.service.ScheduledCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled-orders")
public class ScheduledCartController {

        @Autowired
        ScheduledCartService scheduledCartService;

        @GetMapping("/{id}")
        public ResponseEntity<ScheduledCartDto> getCart(@PathVariable Long id){
            return ResponseEntity.ok().body(scheduledCartService.get(id));
        }

        @PostMapping()
        public ResponseEntity<ScheduledCartDto> createCart (@RequestBody ScheduledCartDto scheduledCartDto) {
            return ResponseEntity.status(201).body(scheduledCartService.create(scheduledCartDto));
        }

        @PutMapping("/{id}")
        public ResponseEntity<ScheduledCartDto> updateCart (@PathVariable Long id, @RequestBody ScheduledCartDto scheduledCartDto){
            return ResponseEntity.status(201).body(scheduledCartService.updateCart(id, scheduledCartDto));
        }
}



