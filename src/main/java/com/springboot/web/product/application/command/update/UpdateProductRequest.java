package com.springboot.web.product.application.command.update;

import com.springboot.web.common.application.mediator.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase UpdateProductRequest que representa la solicitud para actualizar un producto existente.
 * Implementa la interfaz Request con un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest implements Request<Void> {

    private Long id;
    private String name;
    private String description;
    private Double price;
    private MultipartFile file;

}
