package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Seller;
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
public class SellerDto {

    private Long id;

    @NotNull
    @NotEmpty
    private String name;

    public static Seller convert (SellerDto sellerDto){
        return Seller.builder().name(sellerDto.getName()).build();
    }

    public static SellerDto convert (Seller seller){
        return SellerDto.builder().id(seller.getId()).name(seller.getName()).build();
    }
}
