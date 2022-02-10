package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Agent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    private Long sectionId;

    public static AgentDto convert(Agent agent) {
        return AgentDto.builder().id(agent.getId()).name(agent.getName()).sectionId(agent.getSection().getId()).build();
    }

    public static Agent convert(AgentDto agentDto) {
        return Agent.builder().name(agentDto.getName()).build();
    }
}
