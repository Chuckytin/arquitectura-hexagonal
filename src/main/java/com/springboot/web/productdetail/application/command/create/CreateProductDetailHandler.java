package com.springboot.web.productdetail.application.command.create;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateProductDetailHandler implements RequestHandler<CreateProductDetailRequest, CreateProductDetailResponse> {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public CreateProductDetailResponse handle(CreateProductDetailRequest request) {
        ProductDetail productDetail = ProductDetail.builder()
                .specifications(request.getSpecifications())
                .warranty(request.getWarranty())
                .provider(request.getProvider())
                .product(Product.builder().id(request.getProductId()).build())
                .build();

        ProductDetail stored = productDetailRepository.upsert(productDetail);
        log.info("ProductDetail created with id {}", stored.getId());
        return new CreateProductDetailResponse(stored);
    }

    @Override
    public Class<CreateProductDetailRequest> getRequestType() {
        return CreateProductDetailRequest.class;
    }
}