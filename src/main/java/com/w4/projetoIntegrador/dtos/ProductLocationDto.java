package com.w4.projetoIntegrador.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductLocationDto {

    private SectionDto section;

    private Long productId;

    private List<BatchDto> batchStockDto;
}
