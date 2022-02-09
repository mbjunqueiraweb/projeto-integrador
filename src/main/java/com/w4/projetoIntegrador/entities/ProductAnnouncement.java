package com.w4.projetoIntegrador.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products_announcements")
public class ProductAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JsonIgnore
    private Product product;

    @ManyToOne
    private Seller seller;

}
