package com.springboot.web.product.infrastructure.api.dto;

import com.springboot.web.review.infrastructure.api.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Clase ProductDto que representa el objeto de transferencia de datos para un producto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private String image;

    private String provider;

    private List<ReviewDto> reviews;

}
