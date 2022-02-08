package com.w4.projetoIntegrador.dtos;

import com.w4.projetoIntegrador.entities.Product;
import com.w4.projetoIntegrador.enums.ProductTypes;
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
public class ProductDto {

    @NotNull
    @NotEmpty
    private String name;

    @NotNull
    @NotEmpty
    private String productType;

    public static Product convert(ProductDto p) {
        ProductTypes type = ProductTypes.valueOf(p.getProductType());
        Product product = Product.builder().name(p.getName()).productType(type).build();
        return product;
    }

    public static ProductDto convert(Product product) {
        return ProductDto.builder().name(product.getName()).productType(product.getProductType().toString()).build();
    }
}
