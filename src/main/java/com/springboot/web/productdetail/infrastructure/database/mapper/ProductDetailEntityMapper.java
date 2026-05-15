package com.springboot.web.productdetail.infrastructure.database.mapper;

import com.springboot.web.productdetail.domain.entity.ProductDetail;
import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import org.mapstruct.*;

/**
 * Mapper entre la entidad de base de datos ProductDetailEntity y el objeto de dominio ProductDetail.
 * ---
 * Ambas clases forman una relación bidireccional con Product/ProductEntity:
 * ProductDetail.product - ProductDetailEntity.productEntity
 * ---
 * Este campo circular debe ignorarse en ambas direcciones para evitar
 * recursión infinita durante el mapeo. El patrón a seguir es siempre:
 * - @BeanMapping(ignoreUnmappedSourceProperties) para silenciar el campo
 * circular del objeto ORIGEN ante unmappedSourcePolicy = ERROR.
 * - @Mapping(target = ..., ignore = true) para ignorar el campo circular
 * en el objeto DESTINO.
 * ---
 * Si en el futuro ProductDetail adquiere nuevas relaciones bidireccionales,
 * aplica el mismo patrón para cada campo circular nuevo.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR
)
public interface ProductDetailEntityMapper {

    /**
     * product (dominio) es el campo circular — apunta de vuelta a Product.
     * Se ignora en source y en target para cortar la recursión.
     * Los campos escalares (id, specifications, warranty, provider) se mapean automáticamente.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"product"})
    @Mapping(target = "productEntity", ignore = true)
    ProductDetailEntity mapToProductDetailEntity(ProductDetail productDetail);

    /**
     * productEntity (JPA) es el campo circular — apunta de vuelta a ProductEntity.
     * Se ignora en source y en target por el mismo motivo.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"productEntity"})
    @Mapping(target = "product", ignore = true)
    ProductDetail mapToProductDetail(ProductDetailEntity productDetailEntity);
}
