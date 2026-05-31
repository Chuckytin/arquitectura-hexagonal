package com.springboot.web.productdetail.infrastructure.api.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetailDto {

    private Long id;
    private String specifications;
    private String warranty;
    private String provider;
}