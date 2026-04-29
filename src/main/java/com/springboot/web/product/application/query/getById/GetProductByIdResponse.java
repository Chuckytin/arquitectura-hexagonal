package com.springboot.web.product.application.query.getById;

import com.springboot.web.product.domain.entity.Product;
import lombok.*;

/**
 * Clase GetProductByIdResponse que representa la respuesta para obtener un producto por su ID.
 * Contiene un objeto Product que representa el producto obtenido.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductByIdResponse {

    private Product product;

}
