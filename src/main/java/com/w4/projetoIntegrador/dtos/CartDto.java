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

    private LocalDateTime scheduledDateTimeFrom;

    private LocalDateTime scheduledDateTimeTo;

    public static CartDto convert(Cart cart) {

        List<ItemCartDto> itemCartDtoList = new ArrayList<>();

        for (ItemCart itemCart : cart.getItemCarts()) {
            itemCartDtoList.add(ItemCartDto.convert(itemCart));
        }

        return CartDto.builder()
                .id(cart.getId())
                .date(cart.getDate())
                .buyerId(cart.getBuyer().getId())
                .statusCode(cart.getStatusCode())
                .products(itemCartDtoList)
                .build();
        // Não está injetando total price que deve ser injetado na Service
    }

    public static Cart convert(CartDto cartDto) {

        return Cart.builder().date(cartDto.getDate()).statusCode(cartDto.statusCode).build();
        // Não esta injetanfo buyer e itemCarts
    }
}
