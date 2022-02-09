package com.w4.projetoIntegrador.service;

import com.w4.projetoIntegrador.entities.Agent;
import com.w4.projetoIntegrador.entities.Section;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.AgentRepository;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    AgentRepository agentRepository;
    SectionService sectionService;

    public AgentService(AgentRepository agentRepository, SectionService sectionService) {
        this.agentRepository = agentRepository;
        this.sectionService = sectionService;
    }

    public Agent getAgent(Long id) {
        try {
            return agentRepository.findById(id).orElse(null);
        } catch (RuntimeException e) {
            throw new NotFoundException("Agent " + id + " não encontrado na base de dados.");
        }
    }

    public Agent save(Agent agent, Long sectionId) {
        Section section = sectionService.getSection(sectionId);
        agent.setSection(section);
        agent = agentRepository.save(agent);
        return agent;
    }
}
