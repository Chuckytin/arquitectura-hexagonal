package com.springboot.web.product.infrastructure.database.mapper;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.productdetail.infrastructure.database.mapper.ProductDetailEntityMapper;
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
        uses = {ProductDetailEntityMapper.class}
)
public interface ProductEntityMapper {

    /**
     * @Mapping renombra productDetail (dominio) - productDetailEntity (entidad JPA).
     * @BeanMapping ignora productDetail como source sin mapeo restante, ya que
     * la conversión completa la gestiona ProductDetailEntityMapper vía uses.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"productDetail"})
    @Mapping(target = "productDetailEntity", source = "productDetail")
    ProductEntity mapToProductEntity(Product product);

    /**
     * Inverso del anterior: productDetailEntity (entidad JPA) - productDetail (dominio).
     * ProductDetailEntityMapper se encarga de la conversión interna del objeto anidado.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"productDetailEntity"})
    @Mapping(target = "productDetail", source = "productDetailEntity")
    Product mapToProduct(ProductEntity productEntity);
}
