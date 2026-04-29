package com.springboot.web.product.application.query.getAll;

import com.springboot.web.product.domain.entity.Product;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllProductResponse {

    private List<Product> products;

}
