package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Product;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;
import com.w4.projetoIntegrador.entities.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAnnouncementDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String brand;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Float volume;

    @NotNull
    private Float minimumTemperature;

    @NotNull
    private Float maximumTemperature;

    @NotNull
    private Long productId;

    @NotNull
    private Long sellerId;

    public static ProductAnnouncementDto convert(ProductAnnouncement pa) {
        return ProductAnnouncementDto.builder()
                .id(pa.getId())
                .name(pa.getName())
                .brand(pa.getBrand())
                .price(pa.getPrice())
                .volume(pa.getVolume())
                .minimumTemperature(pa.getMinimumTemperature())
                .maximumTemperature(pa.getMaximumTemperature())
                .productId(pa.getProduct().getId())
                .sellerId(pa.getSeller().getId())
                .build();
    }

    public static ProductAnnouncement convert(ProductAnnouncementDto paDto, Seller seller, Product product) {
        return ProductAnnouncement.builder()
                .name(paDto.getName())
                .brand(paDto.getBrand())
                .price(paDto.getPrice())
                .volume(paDto.getVolume())
                .minimumTemperature(paDto.getMinimumTemperature())
                .maximumTemperature(paDto.getMaximumTemperature())
                .product(product)
                .seller(seller)
                .build();
    }
}
