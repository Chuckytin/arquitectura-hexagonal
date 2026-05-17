package com.springboot.web.product.domain.entity;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.review.domain.entity.Review;
import lombok.*;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Product {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;

    private ProductDetail productDetail;

    private List<Review> reviews;

    private List<Category> categories;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
