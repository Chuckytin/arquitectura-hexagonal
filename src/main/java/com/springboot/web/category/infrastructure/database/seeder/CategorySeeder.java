package com.springboot.web.category.infrastructure.database.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.web.category.infrastructure.database.entity.CategoryEntity;
import com.springboot.web.category.infrastructure.database.repository.QueryCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile({"dev", "local"})
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class CategorySeeder implements CommandLineRunner {

    private final QueryCategoryRepository categoryRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @Override
    public void run(String @NonNull ... args) throws Exception {

        if (categoryRepository.count() > 0) {
            log.info("Seeder omitido: ya existen categorías en la base de datos.");
            return;
        }

        Resource resource = resourceLoader.getResource("classpath:seed/categories.json");

        List<CategoryEntity> categories = objectMapper.readValue(
                resource.getInputStream(),
                new TypeReference<>() {
                }
        );

        categoryRepository.saveAll(categories);

        log.info("Seeder completado: {} categorías insertadas desde JSON.", categories.size());
    }
}