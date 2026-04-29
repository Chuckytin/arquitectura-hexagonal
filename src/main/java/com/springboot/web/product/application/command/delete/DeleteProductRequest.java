package com.springboot.web.product.application.command.delete;

import com.springboot.web.common.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase DeleteProductRequest que representa la solicitud para eliminar un producto existente.
 * Implementa la interfaz Request con un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteProductRequest implements Request<Void> {

    private Long id;

}
