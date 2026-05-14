package com.springboot.web.product.infrastructure.database.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infrastructure.database.entity.ProductEntity;
import com.springboot.web.product.infrastructure.database.mapper.ProductEntityMapper;
import com.springboot.web.product.infrastructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
@Slf4j
public class ProductSeeder implements CommandLineRunner {

    private final QueryProductRepository productRepository;
    private final ResourceLoader resourceLoader;
    private final ProductEntityMapper productEntityMapper;

    private final ObjectMapper objectMapper;

    @Override
    public void run(String @NonNull ... args) throws Exception {

        if (productRepository.count() > 0) {
            log.info("Seeder omitido: ya existen productos en la base de datos.");
            return;
        }

        Resource resource = resourceLoader.getResource("classpath:seed/products.json");

        List<Product> products = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {

                });

        List<ProductEntity> productEntities = products.stream()
                .map(productEntityMapper::mapToProductEntity)
                .toList();

        productRepository.saveAll(productEntities);

        log.info("Seeder completado: {} productos insertados desde JSON.", productEntities.size());
    }

}