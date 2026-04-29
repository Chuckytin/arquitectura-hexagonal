package com.springboot.web.product.application.command.delete;

import com.springboot.web.common.mediator.RequestHandler;
import com.springboot.web.common.util.FileUtils;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
