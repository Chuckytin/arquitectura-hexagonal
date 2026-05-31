package com.springboot.web.productdetail.infrastructure.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDetailDto {

    @NotBlank(message = "Specifications are required")
    private String specifications;

    @NotBlank(message = "Warranty is required")
    private String warranty;

    @NotBlank(message = "Provider is required")
    private String provider;

    @NotNull(message = "Product id is required")
    private Long productId;
}