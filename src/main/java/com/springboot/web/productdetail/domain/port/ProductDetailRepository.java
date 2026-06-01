package com.springboot.web.productdetail.domain.port;

import com.springboot.web.productdetail.domain.entity.ProductDetail;

import java.util.Optional;

public interface ProductDetailRepository {

    ProductDetail upsert(ProductDetail productDetail);

    Optional<ProductDetail> findById(Long id);

    Optional<ProductDetail> findByProductId(Long productId);

    boolean existsById(Long id);

    void deleteById(Long id);

    void linkToProduct(Long productDetailId, Long productId);

    void unlinkFromProduct(Long productDetailId);

}