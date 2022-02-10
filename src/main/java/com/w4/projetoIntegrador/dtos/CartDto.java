package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Cart;
import com.w4.projetoIntegrador.entities.ItemCart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDto {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long buyerId;

    private String statusCode;

    private List<ItemCartDto> products;

    private BigDecimal totalPrice;


    public static CartDto convert(Cart cart) {

        List<ItemCartDto> itemCartDtos = new ArrayList<>();

        for (ItemCart itemCart : cart.getItemCarts()) {
            itemCartDtos.add(ItemCartDto.convert(itemCart));
        }

        return CartDto.builder()
                .id(cart.getId())
                .date(cart.getDate())
                .buyerId(cart.getBuyer().getId())
                .statusCode(cart.getStatusCode())
                .products(itemCartDtos)
                .build();
    }
}
