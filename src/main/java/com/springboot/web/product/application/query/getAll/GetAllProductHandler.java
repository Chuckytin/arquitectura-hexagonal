package com.springboot.web.product.application.query.getAll;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

        PaginationResult<Product> products = productRepository.findAll(request.getPaginationQuery());

        return new GetAllProductResponse(products);
    }

    @Override
    public Class<GetAllProductRequest> getRequestType() {
        return GetAllProductRequest.class;
    }
}
