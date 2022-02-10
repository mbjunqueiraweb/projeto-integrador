package com.w4.projetoIntegrador.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "scheduledCarts")
public class ScheduledCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String statusCode;

    private LocalDateTime scheduledDateTimeFrom;

    private LocalDateTime scheduledDateTimeTo;

    @OneToMany(mappedBy = "scheduledCart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ScheduledItemCart> scheduledItemCarts;

    @ManyToOne
    private Buyer buyer;
}
