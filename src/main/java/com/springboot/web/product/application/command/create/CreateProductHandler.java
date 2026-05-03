package com.springboot.web.product.application.command.create;

import com.springboot.web.common.mediator.RequestHandler;
import com.springboot.web.common.util.FileUtils;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Clase CreateProductHandler que maneja la lógica de negocio para crear un nuevo producto.
 * Implementa la interfaz RequestHandler con un tipo de solicitud CreateProductRequest y un tipo de respuesta Void,
 * lo que indica que no se espera una respuesta específica después de manejar esta solicitud.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateProductHandler implements RequestHandler<CreateProductRequest, CreateProductResponse> {

    private final ProductRepository productRepository;
    private final FileUtils fileUtils;

    @Override
    public CreateProductResponse handle(CreateProductRequest request) {

        String image = null;

        if (request.getFile() != null && !request.getFile().isEmpty()) {
            image = fileUtils.saveProductImage(request.getFile());
        }

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .image(image)
                .build();

        Product storedProduct = productRepository.upsert(product);

        log.info("Product created with id {}", storedProduct.getId());

        return new CreateProductResponse(storedProduct);
    }

    @Override
    public Class<CreateProductRequest> getRequestType() {
        return CreateProductRequest.class;
    }

}
