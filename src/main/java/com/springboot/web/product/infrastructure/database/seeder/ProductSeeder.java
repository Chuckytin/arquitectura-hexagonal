package com.springboot.web.product.infrastructure.database.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.web.category.infrastructure.database.entity.CategoryEntity;
import com.springboot.web.category.infrastructure.database.repository.QueryCategoryRepository;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.product.infrastructure.database.repository.QueryProductRepository;
import com.springboot.web.productdetail.infrastructure.database.entity.ProductDetailEntity;
import com.springboot.web.review.infrastructure.database.entity.ReviewEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Profile({"dev", "local"})
@Order(2)
@RequiredArgsConstructor
@Slf4j
public class ProductSeeder implements CommandLineRunner {

    private final QueryProductRepository productRepository;
    private final QueryCategoryRepository categoryRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    /**
     * DTO interno solo para deserializar el JSON — nombres coinciden exactamente con el JSON
     */
    @Getter
    @Setter
    @NoArgsConstructor
    static class ProductSeedDto {
        private String name;
        private String description;
        private Double price;
        private String image;
        private ProductDetailEntity productDetail;
        private List<ReviewEntity> reviews = new ArrayList<>();
        private List<CategoryEntity> categories = new ArrayList<>();
    }

    @Override
    public void run(String @NonNull ... args) throws Exception {

        if (productRepository.count() > 0) {
            log.info("Seeder omitido: ya existen productos en la base de datos.");
            return;
        }

        Resource resource = resourceLoader.getResource("classpath:seed/products.json");

        List<ProductSeedDto> seedDtos = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {
                }
        );

        List<ProductEntity> productsEntity = seedDtos.stream()
                .map(dto -> {
                    Set<CategoryEntity> persistedCategories = dto.getCategories().stream()
                            .map(cat -> categoryRepository.findByName(cat.getName())
                                    .orElseThrow(() -> new IllegalStateException(
                                            "Categoría no encontrada: " + cat.getName())))
                            .collect(Collectors.toSet());

                    ProductEntity productEntity = ProductEntity.builder()
                            .name(dto.getName())
                            .description(dto.getDescription())
                            .price(dto.getPrice())
                            .image(dto.getImage())
                            .productDetail(dto.getProductDetail())
                            .reviews(new HashSet<>(dto.getReviews()))
                            .categories(persistedCategories)
                            .build();

                    // Establece la FK product_id en cada review
                    productEntity.getReviews()
                            .forEach(review -> review.setProduct(productEntity));

                    return productEntity;
                })
                .toList();

        productRepository.saveAll(productsEntity);

        log.info("Seeder completado: {} productos insertados desde JSON.", productsEntity.size());
    }
}