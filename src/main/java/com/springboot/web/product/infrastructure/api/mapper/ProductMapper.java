package com.springboot.web.product.infrastructure.api.mapper;

import com.springboot.web.category.domain.entity.Category;
import com.springboot.web.product.application.command.create.CreateProductRequest;
import com.springboot.web.product.application.command.update.UpdateProductRequest;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infrastructure.api.dto.CreateProductDto;
import com.springboot.web.product.infrastructure.api.dto.ProductDto;
import com.springboot.web.product.infrastructure.api.dto.UpdateProductDto;
import com.springboot.web.review.domain.entity.Review;
import com.springboot.web.review.infrastructure.api.dto.ReviewDto;
import org.mapstruct.*;

/**
 * Mapper entre DTOs de la capa API y objetos del dominio/aplicación.
 * unmappedSourcePolicy = ERROR garantiza que cualquier campo nuevo que se añada
 * a los objetos fuente rompa en compilación si no tiene mapeo explícito, evitando pérdidas silenciosas de datos.
 * ---
 * Si se añade un nuevo campo a ProductDto que venga de un objeto anidado de Product
 * (como productDetail.provider), se seguirá el mismo patrón:
 * - @Mapping(target = "nuevocampo", source = "objetoAnidado.nuevocampo")
 * - Se añade "objetoAnidado" a ignoreUnmappedSourceProperties si no todos sus campos tienen destino en el DTO.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedSourcePolicy = ReportingPolicy.ERROR)
public interface ProductMapper {

    CreateProductRequest mapToCreateProductRequest(CreateProductDto createProductDto);

    UpdateProductRequest mapToUpdateProductRequest(UpdateProductDto updateProductDto);

    /**
     * - provider viene del path productDetail.provider.
     * - reviews: mapToReviewDto gestiona la conversión Review - ReviewDto.
     * - categories: mapToCategoyName extrae solo el nombre de cada Category
     * para exponerlo como List<String> en el DTO, ya que no necesitamos
     * exponer el objeto Category completo.
     */
    @Mapping(target = "provider", source = "productDetail.provider")
    @Mapping(target = "categories", source = "categories")
    @BeanMapping(ignoreUnmappedSourceProperties = {"productDetail"})
    ProductDto mapToProductDto(Product product);

    /**
     * Mapea la lista de reviews dentro de mapToProductDto.
     * Review.product es circular y no tiene destino en ReviewDto, se ignora en source.
     */
    @BeanMapping(ignoreUnmappedSourceProperties = {"product"})
    ReviewDto mapToReviewDto(Review review);

    @Mapping(target = "product", ignore = true)
    Review mapToReview(ReviewDto reviewDto);

    /**
     * Extrae solo el nombre de Category para List<String> en ProductDto.
     * Category.products es circular y no tiene destino en String, se ignora.
     */
    default String mapToCategoryName(Category category) {
        return category == null ? null : category.getName();
    }

}
