package com.springboot.web.product.application.command.delete;

import com.springboot.web.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductRequest implements Request<Void> {

    private Long id;

}
