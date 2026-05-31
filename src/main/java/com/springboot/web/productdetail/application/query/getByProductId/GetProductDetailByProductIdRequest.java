package com.springboot.web.productdetail.application.query.getByProductId;

import com.springboot.web.common.application.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDetailByProductIdRequest implements Request<GetProductDetailByProductIdResponse> {

    private Long productId;
}