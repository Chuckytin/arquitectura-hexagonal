package com.springboot.web.product.application.query.getAll;

import com.springboot.web.common.mediator.RequestHandler;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Clase GetAllProductHandler que maneja la lógica de negocio para obtener todos los productos.
 * Implementa la interfaz RequestHandler con un tipo de solicitud GetAllProductRequest y un tipo de respuesta GetAllProductResponse,
 * lo que indica que se espera una respuesta específica después de manejar esta
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetAllProductHandler implements RequestHandler<GetAllProductRequest, GetAllProductResponse> {

    private final ProductRepository productRepository;

    @Override
    public GetAllProductResponse handle(GetAllProductRequest request) {

        log.info("GetAllProductHandler start");

        List<Product> products = productRepository.findAll();

        log.info("Found {} products", products.size());

        return new GetAllProductResponse(products);
    }

    @Override
    public Class<GetAllProductRequest> getRequestType() {
        return GetAllProductRequest.class;
    }
}
