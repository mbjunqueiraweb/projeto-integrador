package com.w4.projetoIntegrador.controller;

import com.w4.projetoIntegrador.dtos.AgentDto;
import com.w4.projetoIntegrador.entities.Agent;
import com.w4.projetoIntegrador.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    AgentService agentService;

    @GetMapping("/{id}")
    public ResponseEntity<AgentDto> getAgent(@PathVariable Long id) {
        return ResponseEntity.ok().body(AgentDto.convert(agentService.getAgent(id)));
    }

    @PostMapping("/")
    public ResponseEntity<AgentDto> newAgent(@Valid @RequestBody AgentDto agentDto) {
       Agent agent =  agentService.save(AgentDto.convert(agentDto), agentDto.getSectionId());
       return ResponseEntity.status(201).body(AgentDto.convert(agent));
    }
}


