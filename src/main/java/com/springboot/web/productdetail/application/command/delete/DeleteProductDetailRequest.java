package com.springboot.web.productdetail.application.command.delete;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductDetailRequest implements Request<Void> {

    private Long id;
}