package com.springboot.web.productdetail.domain.entity;

import com.springboot.web.product.domain.entity.Product;
import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDetail {

    private Long id;
    private String specifications;
    private String warranty;
    private String provider;

    private Product product;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetail that = (ProductDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
