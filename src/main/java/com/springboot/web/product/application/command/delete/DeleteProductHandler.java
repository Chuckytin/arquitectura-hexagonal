package com.springboot.web.product.application.command.delete;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.common.infrastructure.util.FileUtils;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Clase DeleteProductHandler que maneja la lógica de negocio para eliminar un producto existente.
 * Implementa la interfaz RequestHandler con un tipo de solicitud DeleteProductRequest y un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductHandler implements RequestHandler<DeleteProductRequest, Void> {

    private final ProductRepository productRepository;

    private final FileUtils fileUtils;

    @Override
    public Void handle(DeleteProductRequest request) {

        Long productId = request.getId();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id " + productId));

        String imageName = product.getImage();

        if (imageName != null) {
            fileUtils.deleteProductImage(imageName);
        }

        productRepository.deleteById(productId);

        log.debug("Product deleted with id {}", productId);

        return null;
    }

    @Override
    public Class<DeleteProductRequest> getRequestType() {
        return DeleteProductRequest.class;
    }

}
