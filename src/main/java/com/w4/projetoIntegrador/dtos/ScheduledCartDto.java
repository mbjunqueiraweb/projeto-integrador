package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Cart;
import com.w4.projetoIntegrador.entities.ItemCart;
import com.w4.projetoIntegrador.entities.ScheduledCart;
import com.w4.projetoIntegrador.entities.ScheduledItemCart;
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
public class ScheduledCartDto {

    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long buyerId;

    private String statusCode;

    private List<ScheduledItemCartDto> products;

    private BigDecimal totalPrice;

    private LocalDateTime scheduledDateTimeFrom;

    private LocalDateTime scheduledDateTimeTo;

    public static ScheduledCartDto convert(ScheduledCart scheduledCart) {

        List<ScheduledItemCartDto> scheduledItemCartDtos = new ArrayList<>();

        for (ScheduledItemCart scheduledItemCart : scheduledCart.getScheduledItemCarts()) {
            scheduledItemCartDtos.add(ScheduledItemCartDto.convert(scheduledItemCart));
        }

        return ScheduledCartDto.builder()
                .id(scheduledCart.getId())
                .date(scheduledCart.getDate())
                .buyerId(scheduledCart.getBuyer().getId())
                .statusCode(scheduledCart.getStatusCode())
                .products(scheduledItemCartDtos)
                .scheduledDateTimeFrom(scheduledCart.getScheduledDateTimeFrom())
                .scheduledDateTimeTo(scheduledCart.getScheduledDateTimeTo())
                .build();
        // Não está injetando total price que deve ser injetado na Service
    }

//    public static ScheduledCart convert(ScheduledCartDto scheduledCartDto) {
//
//        return ScheduledCart.builder().date(scheduledCartDto.getDate()).statusCode(scheduledCartDto.statusCode).build();
//        // Não esta injetanfo buyer e itemCarts
//    }
}
