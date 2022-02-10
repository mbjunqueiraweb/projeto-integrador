package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Section;
import com.w4.projetoIntegrador.entities.Warehouse;
import com.w4.projetoIntegrador.enums.ProductTypes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectionDto {

    private Long id;

    @NotNull
    private ProductTypes type;

    @NotNull
    private float totalSpace;

    @NotNull
    private Long warehouseId;

    public static Section convert (SectionDto sectionDto, Warehouse warehouse){
        return  Section.builder()
                .type(sectionDto.getType())
                .totalSpace(sectionDto.getTotalSpace())
                .warehouse(warehouse)
                .build();
    }

    public static SectionDto convert (Section section){
        return SectionDto.builder()
                .id(section.getId())
                .type(section.getType())
                .totalSpace(section.getTotalSpace())
                .warehouseId(section.getWarehouse().getId())
                .build();
    }
}
