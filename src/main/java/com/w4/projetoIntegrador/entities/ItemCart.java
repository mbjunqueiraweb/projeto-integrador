package com.w4.projetoIntegrador.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "items_cart")
public class ItemCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProductAnnouncement productAnnouncement;

    @ManyToOne
    private Cart cart;

    @NotNull
    private Integer quantity;
}
