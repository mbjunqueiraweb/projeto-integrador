package com.w4.projetoIntegrador.dtos;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.w4.projetoIntegrador.entities.Batch;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchDto {

    private Long id;

    @NotNull
    private Integer initialQuantity;
    private Integer stock;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @NotNull
    private LocalDateTime manufacturingDateTime;

    @NotNull
    private LocalDate dueDate;

    @NotNull
    private Float currentTemperature;

    @NotNull
    private Long productId;

    public static Batch convert(BatchDto batchDto, ProductAnnouncement pa, Integer stock) {
        Batch batch = Batch.builder()
                .dueDate(batchDto.getDueDate())
                .currentTemperature(batchDto.getCurrentTemperature())
                .manufacturingDateTime(batchDto.getManufacturingDateTime())
                .productAnnouncement(pa)
                .stock(stock)
                .initialQuantity(batchDto.getInitialQuantity())
                .build();

        return batch;
    }

    public static BatchDto convert(Batch batch) {
        return BatchDto.builder()
                .id(batch.getId())
                .initialQuantity(batch.getInitialQuantity())
                .manufacturingDateTime(batch.getManufacturingDateTime())
                .dueDate(batch.getDueDate())
                .stock(batch.getStock())
                .currentTemperature(batch.getCurrentTemperature())
                .productId(batch.getProductAnnouncement().getId())
                .build();
    }
}
