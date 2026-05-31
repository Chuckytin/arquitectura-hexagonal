package com.springboot.web.productdetail.application.query.getById;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.productdetail.domain.exception.ProductDetailNotFoundException;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetProductDetailByIdHandler implements RequestHandler<GetProductDetailByIdRequest, GetProductDetailByIdResponse> {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public GetProductDetailByIdResponse handle(GetProductDetailByIdRequest request) {
        log.info("GetProductDetailByIdHandler start for id {}", request.getId());
        return productDetailRepository.findById(request.getId())
                .map(GetProductDetailByIdResponse::new)
                .orElseThrow(() -> new ProductDetailNotFoundException(request.getId()));
    }

    @Override
    public Class<GetProductDetailByIdRequest> getRequestType() {
        return GetProductDetailByIdRequest.class;
    }
}