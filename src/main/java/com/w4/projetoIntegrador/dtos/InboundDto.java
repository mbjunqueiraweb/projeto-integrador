package com.w4.projetoIntegrador.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.w4.projetoIntegrador.entities.Agent;
import com.w4.projetoIntegrador.entities.Batch;
import com.w4.projetoIntegrador.entities.Inbound;
import com.w4.projetoIntegrador.entities.Section;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InboundDto {

    private Long orderNumber;

    @JsonAlias("orderDate")
    private LocalDateTime date;

    @NotNull
    private Long sectionId;

    @NotNull
    private Long agentId;

    @Valid
    @NotNull
    @JsonAlias("batchList")
    private List<BatchDto> batchDtoList;

    public static Inbound convert(InboundDto idto, List<Batch> batchList, Section s) {
       Inbound inbound = Inbound.builder().date(idto.getDate()).batchList(batchList).section(s).build();
       return inbound;
    }

    public static Inbound convert(InboundDto idto, List<Batch> batchList, Section s, Agent agent) {
        Inbound inbound = Inbound.builder().date(idto.getDate()).batchList(batchList).section(s).agent(agent).build();
        return inbound;
    }

    public static InboundDto convert(Inbound inbound){
       List<BatchDto> batchDtoList = new ArrayList<>();

       for (Batch batch: inbound.getBatchList()){
           batchDtoList.add(BatchDto.convert(batch));
       }

        return InboundDto.builder()
                .orderNumber(inbound.getId())
                .date(inbound.getDate())
                .sectionId(inbound.getSection().getId())
                .agentId(inbound.getAgent().getId())
                .batchDtoList(batchDtoList)
                .build();
    }
}
