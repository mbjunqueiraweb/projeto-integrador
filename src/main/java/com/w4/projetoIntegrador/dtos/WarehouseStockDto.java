package com.w4.projetoIntegrador.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseStockDto {
    private Long warehosecode;
    private Integer totalquantity;
    private Long section;
    private Long batch;
}
