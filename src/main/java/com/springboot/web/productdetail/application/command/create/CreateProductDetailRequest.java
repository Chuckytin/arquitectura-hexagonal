package com.springboot.web.productdetail.application.command.create;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductDetailRequest implements Request<CreateProductDetailResponse> {

    private String specifications;
    private String warranty;
    private String provider;
    private Long productId;
}