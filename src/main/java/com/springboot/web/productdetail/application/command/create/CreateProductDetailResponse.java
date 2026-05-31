package com.springboot.web.productdetail.application.command.create;

import com.springboot.web.productdetail.domain.entity.ProductDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDetailResponse {

    private ProductDetail productDetail;
}