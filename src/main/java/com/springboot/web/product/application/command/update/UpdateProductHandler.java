package com.springboot.web.product.application.command.update;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.category.infrastructure.database.mapper.CategoryEntityMapper;
import com.springboot.web.category.infrastructure.database.repository.QueryCategoryRepository;
import com.springboot.web.common.application.mediator.RequestHandler;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.exception.ProductNotFoundException;
import com.springboot.web.product.domain.port.ProductRepository;
import com.springboot.web.productdetail.domain.entity.ProductDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
    private final QueryCategoryRepository queryCategoryRepository;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    public Void handle(UpdateProductRequest request) {

        Product product = productRepository.findById(request.getId())
                .orElseThrow(() -> new ProductNotFoundException(request.getId()));

        ProductDetail productDetail = product.getProductDetail();

        if (productDetail == null) {
            productDetail = new ProductDetail();
            product.setProductDetail(productDetail);
        }

        productDetail.setProvider(request.getProvider());

        if (request.getReview() != null) {
            if (product.getReviews() == null) {
                product.setReviews(new ArrayList<>());
            }
            product.getReviews().add(request.getReview());
        }

        if (request.getCategoryId() != null) {
            Category category = queryCategoryRepository.findById(request.getCategoryId())
                    .map(categoryEntityMapper::mapToCategory)
                    .orElseThrow(() -> new RuntimeException("Category not found"));

            if (product.getCategories() == null) {
                product.setCategories(new ArrayList<>());
            }
            product.getCategories().add(category);
        }

        productRepository.upsert(product);

        log.debug("Product updated with id {}", product.getId());

        return null;
    }

    @Override
    public Class<UpdateProductRequest> getRequestType() {
        return UpdateProductRequest.class;
    }

}
