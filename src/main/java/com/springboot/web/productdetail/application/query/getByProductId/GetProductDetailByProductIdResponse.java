package com.springboot.web.productdetail.application.query.getByProductId;

import com.springboot.web.productdetail.domain.entity.ProductDetail;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDetailByProductIdResponse {

    private ProductDetail productDetail;
}