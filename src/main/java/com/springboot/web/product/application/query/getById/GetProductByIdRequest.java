package com.springboot.web.product.application.query.getById;

import com.springboot.web.common.mediator.Request;
import lombok.*;

/**
 * Clase GetProductByIdRequest que representa la solicitud para obtener un producto por su ID.
 * Implementa la interfaz Request con un tipo de respuesta GetProductByIdResponse,
 * lo que indica que se espera una respuesta específica después de manejar esta solicitud.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetProductByIdRequest implements Request<GetProductByIdResponse> {

    private Long id;

}
