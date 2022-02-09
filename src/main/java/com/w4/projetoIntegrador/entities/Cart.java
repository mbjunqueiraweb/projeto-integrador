package com.w4.projetoIntegrador.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

//    @Transient
//    private Long buyerId;

    private String statusCode;

    private LocalDateTime scheduledDateTimeFrom;

    private LocalDateTime scheduledDateTimeTo;

    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<ItemCart> itemCarts;

    @ManyToOne
    private Buyer buyer;

}
