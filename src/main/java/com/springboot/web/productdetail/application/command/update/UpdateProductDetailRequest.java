package com.springboot.web.productdetail.application.command.update;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductDetailRequest implements Request<Void> {

    private Long id;
    private String specifications;
    private String warranty;
    private String provider;
}