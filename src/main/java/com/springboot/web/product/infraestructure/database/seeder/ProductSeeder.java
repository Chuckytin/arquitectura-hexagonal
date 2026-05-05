package com.springboot.web.product.infraestructure.database.seeder;

import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.infraestructure.database.entity.ProductEntity;
import com.springboot.web.product.infraestructure.database.mapper.ProductEntityMapper;
import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Stream;

@Component
@Profile({"dev", "local"})
@RequiredArgsConstructor
@Slf4j
public class ProductSeeder implements CommandLineRunner {

    private final QueryProductRepository queryProductRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public void run(String @NonNull ... args) {

        if (!queryProductRepository.existsByName("Product 1")) {

            List<ProductEntity> entities = Stream.of(
                            Product.builder()
                                    .name("Product 1")
                                    .description("Description 1")
                                    .price(10.0)
                                    .image("image1.jpg")
                                    .build(),
                            Product.builder()
                                    .name("Product 2")
                                    .description("Description 2")
                                    .price(20.0)
                                    .image("image2.jpg")
                                    .build(),
                            Product.builder()
                                    .name("Product 3")
                                    .description("Description 3")
                                    .price(30.0)
                                    .image("image3.jpg")
                                    .build()
                    )
                    .map(productEntityMapper::mapToProductEntity)
                    .toList();

            queryProductRepository.saveAll(entities);

            log.info("Seeder completado: se han insertado productos de prueba en la base de datos.");

        } else {
            log.info("Seeder omitido: ya existen productos de prueba en la base de datos.");
        }
    }
}