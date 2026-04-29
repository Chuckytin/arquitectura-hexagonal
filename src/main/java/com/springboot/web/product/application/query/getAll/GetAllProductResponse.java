package com.springboot.web.product.application.query.getAll;

import com.springboot.web.product.domain.entity.Product;
import lombok.*;

import java.util.List;

/**
 * Clase GetAllProductResponse que representa la respuesta para obtener todos los productos.
 * Contiene una lista de objetos Product que representan los productos obtenidos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllProductResponse {

    private List<Product> products;

}
