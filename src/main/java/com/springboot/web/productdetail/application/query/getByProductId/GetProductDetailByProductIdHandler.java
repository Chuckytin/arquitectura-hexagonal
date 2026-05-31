package com.springboot.web.productdetail.application.query.getByProductId;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.productdetail.domain.exception.ProductDetailNotFoundException;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProductDetailByProductIdHandler implements RequestHandler<GetProductDetailByProductIdRequest, GetProductDetailByProductIdResponse> {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public GetProductDetailByProductIdResponse handle(GetProductDetailByProductIdRequest request) {
        log.info("GetProductDetailByProductIdHandler start for productId {}", request.getProductId());
        return productDetailRepository.findByProductId(request.getProductId())
                .map(GetProductDetailByProductIdResponse::new)
                .orElseThrow(() -> new ProductDetailNotFoundException(request.getProductId()));
    }

    @Override
    public Class<GetProductDetailByProductIdRequest> getRequestType() {
        return GetProductDetailByProductIdRequest.class;
    }
}