package com.springboot.web.product.infrastructure.database.mapper;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.productdetail.infrastructure.database.mapper.ProductDetailEntityMapper;
import com.springboot.web.review.infrastructure.mapper.ReviewEntityMapper;
import org.mapstruct.*;

/**
 * Mapper entre la entidad de base de datos ProductEntity y el objeto de dominio Product.
 * ---
 * uses = {ProductDetailEntityMapper.class} delega la conversión del objeto anidado
 * productDetail - productDetailEntity a su mapper especializado. Sin esto, MapStruct
 * no sabría convertir entre ProductDetail y ProductDetailEntity y fallaría en compilación.
 * ---
 * Si se añade una nueva relación @OneToOne o @ManyToOne a ProductEntity con su
 * correspondiente objeto de dominio, crea un mapper específico para ese par
 * y añádelo al uses de este mapper siguiendo el mismo patrón.
 */
@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        uses = {ProductDetailEntityMapper.class, ReviewEntityMapper.class}
)
public interface ProductEntityMapper {

    /**
     * @Mapping renombra productDetail (dominio) - productDetailEntity (entidad JPA).
     * @BeanMapping ignora productDetail como source sin mapeo restante, ya que
     * la conversión completa la gestiona ProductDetailEntityMapper vía uses.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"productDetail"})
    @Mapping(target = "productDetailEntity", source = "productDetail")
    @Mapping(target = "reviewsEntity", source = "reviews")
    ProductEntity mapToProductEntity(Product product);

    /**
     * Inverso del anterior: productDetailEntity (entidad JPA) - productDetail (dominio).
     * ProductDetailEntityMapper se encarga de la conversión interna del objeto anidado.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"productDetailEntity"})
    @Mapping(target = "productDetail", source = "productDetailEntity")
    @Mapping(target = "reviews", source = "reviewsEntity")
    Product mapToProduct(ProductEntity productEntity);

    /**
     * Establece la relación inversa review - product después del mapeo automático.
     * Sin esto, las reviews se guardan con product_id = NULL porque MapStruct no maneja
     * automáticamente las relaciones bidireccionales de JPA.
     */
    @AfterMapping
    default void linkReviews(@MappingTarget ProductEntity productEntity, Product product) {
        if (productEntity.getReviewsEntity() != null) {
            productEntity.getReviewsEntity().forEach(review -> review.setProductEntity(productEntity));
        }
    }

}
