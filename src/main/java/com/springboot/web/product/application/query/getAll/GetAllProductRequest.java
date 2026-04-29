package com.springboot.web.product.application.query.getAll;

import com.springboot.web.common.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllProductRequest implements Request<GetAllProductResponse> {

    private Long id;

}
