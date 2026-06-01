package com.springboot.web.productdetail.application.command.delete;

import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.productdetail.domain.exception.ProductDetailNotFoundException;
import com.springboot.web.productdetail.domain.port.ProductDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteProductDetailHandler implements RequestHandler<DeleteProductDetailRequest, Void> {

    private final ProductDetailRepository productDetailRepository;

    @Override
    public Void handle(DeleteProductDetailRequest request) {
        if (!productDetailRepository.existsById(request.getId())) {
            throw new ProductDetailNotFoundException(request.getId());
        }
        productDetailRepository.unlinkFromProduct(request.getId());
        productDetailRepository.deleteById(request.getId());
        log.info("ProductDetail deleted with id {}", request.getId());
        return null;
    }

    @Override
    public Class<DeleteProductDetailRequest> getRequestType() {
        return DeleteProductDetailRequest.class;
    }
}