package com.springboot.web.product.application.command.create;

import com.springboot.web.common.application.mediator.Request;
import com.springboot.web.product.domain.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase CreateProductRequest que representa la solicitud para crear un nuevo producto.
 * Implementa la interfaz Request con un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductResponse implements Request<Void> {

    private Product product;

}
