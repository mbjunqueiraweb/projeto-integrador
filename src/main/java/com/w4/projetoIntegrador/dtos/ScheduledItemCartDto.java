package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduledItemCartDto {

    private Long id;

    @NotNull
    private Integer quantity;

    @NotNull
    private Long productAnnouncementId;

    public static ScheduledItemCartDto convert(ScheduledItemCart scheduledItemCart) {
        return ScheduledItemCartDto.builder()
                .id(scheduledItemCart.getId())
                .quantity(scheduledItemCart.getQuantity())
                .productAnnouncementId(scheduledItemCart.getProductAnnouncement().getId())
                .build();
    }

    public static ScheduledItemCart convert(ScheduledItemCartDto scheduledItemCartDto,
                                            ProductAnnouncement p,
                                            ScheduledCart c) {
        return ScheduledItemCart.builder()
                .scheduledCart(c)
                .productAnnouncement(p)
                .quantity(scheduledItemCartDto.getQuantity())
                .build();
    }
}
