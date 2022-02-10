package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Cart;
import com.w4.projetoIntegrador.entities.ItemCart;
import com.w4.projetoIntegrador.entities.ProductAnnouncement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCartDto {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private Long productAnnouncementId;

    public static ItemCartDto convert(ItemCart itemCart){
        return ItemCartDto.builder()
                .id(itemCart.getId())
                .quantity(itemCart.getQuantity())
                .productAnnouncementId(itemCart.getProductAnnouncement().getId())
                .build();
    }

    public static ItemCart convert(ItemCartDto itemCartDto, ProductAnnouncement p, Cart c) {
        return ItemCart.builder()
                .cart(c)
                .productAnnouncement(p)
                .quantity(itemCartDto.getQuantity())
                .build();
    }

}
