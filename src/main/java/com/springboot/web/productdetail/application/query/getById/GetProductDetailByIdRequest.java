package com.springboot.web.productdetail.application.query.getById;

import com.springboot.web.common.application.mediator.Request;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductDetailByIdRequest implements Request<GetProductDetailByIdResponse> {

    private Long id;
}