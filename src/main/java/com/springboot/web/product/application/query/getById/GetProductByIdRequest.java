package com.springboot.web.product.application.query.getById;

import com.springboot.web.common.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductByIdRequest implements Request<GetProductByIdResponse> {

    private Long id;

}
