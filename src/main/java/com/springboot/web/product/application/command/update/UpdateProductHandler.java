package com.springboot.web.product.application.command.update;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.common.infraestructure.util.FileUtils;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Clase UpdateProductHandler que maneja la lógica de negocio para actualizar un producto existente.
 * Implementa la interfaz RequestHandler con un tipo de solicitud UpdateProductRequest y un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProductHandler implements RequestHandler<UpdateProductRequest, Void> {

    private final ProductRepository productRepository;
    private final FileUtils fileUtils;

    @Override
    public Void handle(UpdateProductRequest request) {

        Long productId = request.getId();

        Product existing = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id " + productId));

        String image = existing.getImage();

        if (request.getFile() != null && !request.getFile().isEmpty()) {

            if (image != null) {
                fileUtils.deleteProductImage(image);
            }

            image = fileUtils.saveProductImage(request.getFile());
        }

//        Product product = Product.builder()
//                .id(request.getId())
//                .name(request.getName())
//                .description(request.getDescription())
//                .price(request.getPrice())
//                .image(image)
//                .build();

        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setImage(image);

        productRepository.upsert(existing);

        productRepository.upsert(existing);

        log.debug("Product updated with id {}", productId);

        return null;
    }

    @Override
    public Class<UpdateProductRequest> getRequestType() {
        return UpdateProductRequest.class;
    }

}
