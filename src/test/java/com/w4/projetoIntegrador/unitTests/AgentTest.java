package com.w4.projetoIntegrador.unitTests;

import com.w4.projetoIntegrador.entities.Agent;
import com.w4.projetoIntegrador.entities.Section;
import com.w4.projetoIntegrador.enums.ProductTypes;
import com.w4.projetoIntegrador.exceptions.NotFoundException;
import com.w4.projetoIntegrador.repository.AgentRepository;
import com.w4.projetoIntegrador.service.AgentService;
import com.w4.projetoIntegrador.service.SectionService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgentTest {

    private Section section = Section.builder().id(1L).totalSpace(24F).type(ProductTypes.congelado).build();

    private Agent agent = Agent.builder().id(12L).name("agent").section(section).build();

    private AgentRepository agentRepository;
    private SectionService sectionService;
    private AgentService agentService;

    @BeforeEach
    public void setup() {
        agentRepository = Mockito.mock(AgentRepository.class);
        sectionService = Mockito.mock(SectionService.class);
        agentService = new AgentService(agentRepository, sectionService);
    }

    @Test
    public void deveCadastrarUmAgent() {
        Agent agentDto = Agent.builder().name("agent").section(section).build();
        Mockito.when(sectionService.getSection(1L)).thenReturn(section);
        Mockito.when(agentRepository.save(Mockito.any())).thenReturn(agent);
        Agent agentDtoSaved = agentService.save(agentDto, section.getId());
        assertEquals(agentDtoSaved.getId(), 12L);

    }

    @Test
    public void deveRetornarUmAgent() {
        Mockito.when(agentRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(agent));
        Agent agentActual = agentService.getAgent(1L);
        assertEquals(agent, agentActual);
    }

    @Test
    public void deveRetornarUmAgentDto() {
        Mockito.when(agentRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(agent));
        Agent agentActualDto = agentService.getAgent(1L);
        assertEquals(agent.getName(), agentActualDto.getName());
    }

    @Test
    public void deveLancarExpectionQuandoAgentRepositoryLancarExpection() {
        Assertions.assertThrows(NotFoundException.class,() -> {
            Mockito.when(agentRepository.findById(1L)).thenThrow(new RuntimeException());
            Agent agentActual = agentService.getAgent(1L);
            assertEquals(agent, agentActual);
        });
    }
}
