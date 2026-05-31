package com.springboot.web.productdetail.application.query.getById;

import com.springboot.web.productdetail.domain.entity.ProductDetail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDetailByIdResponse {

    private ProductDetail productDetail;
}