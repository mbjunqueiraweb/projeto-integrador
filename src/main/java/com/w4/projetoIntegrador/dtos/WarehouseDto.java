package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Warehouse;
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
public class WarehouseDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    public static WarehouseDto convert(Warehouse warehouse) {
        return WarehouseDto.builder().id(warehouse.getId()).name(warehouse.getName()).build();
    }

    public static Warehouse convert(WarehouseDto warehouseDto){
        return Warehouse.builder().name(warehouseDto.getName()).build();
    }
}
