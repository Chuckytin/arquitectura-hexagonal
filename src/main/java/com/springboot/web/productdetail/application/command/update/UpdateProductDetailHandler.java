package com.springboot.web.productdetail.application.command.update;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.productdetail.domain.exception.ProductDetailNotFoundException;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateProductDetailHandler implements RequestHandler<UpdateProductDetailRequest, Void> {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public Void handle(UpdateProductDetailRequest request) {
        ProductDetail existing = productDetailRepository.findById(request.getId())
                .orElseThrow(() -> new ProductDetailNotFoundException(request.getId()));

        ProductDetail updated = ProductDetail.builder()
                .id(existing.getId())
                .specifications(request.getSpecifications())
                .warranty(request.getWarranty())
                .provider(request.getProvider())
                .product(existing.getProduct())
                .build();

        productDetailRepository.upsert(updated);
        log.info("ProductDetail updated with id {}", request.getId());
        return null;
    }

    @Override
    public Class<UpdateProductDetailRequest> getRequestType() {
        return UpdateProductDetailRequest.class;
    }
}