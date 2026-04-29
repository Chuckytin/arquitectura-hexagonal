package com.springboot.web.product.application.query.getById;

import com.springboot.web.product.domain.entity.Product;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductByIdResponse {

    private Product product;

}
