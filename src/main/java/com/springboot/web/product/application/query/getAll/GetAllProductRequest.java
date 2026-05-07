package com.springboot.web.product.application.query.getAll;

import com.springboot.web.common.application.mediator.Request;
import com.springboot.web.common.domain.PaginationQuery;
import lombok.*;

/**
 * Clase GetAllProductRequest que representa la solicitud para obtener todos los productos.
 * Implementa la interfaz Request con un tipo de respuesta GetAllProductResponse,
 * lo que indica que se espera una respuesta específica después de manejar esta solicitud.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllProductRequest implements Request<GetAllProductResponse> {

    PaginationQuery paginationQuery;

}
