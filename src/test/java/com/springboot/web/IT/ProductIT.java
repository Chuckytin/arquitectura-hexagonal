package com.springboot.web.IT;

import com.springboot.web.IT.config.TestSecurityConfig;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el controlador de productos.
 * Usa MockMvc para todas las peticiones — no RestTemplate — ya que la API usa JWT
 * y @WithMockUser gestiona la autenticación directamente en el SecurityContext.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Slf4j
class ProductIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private Long savedProductId;

    @BeforeEach
    void setUp() {
        log.info(">>> Preparando datos para el test de integración");

        Product saved = productRepository.upsert(
                Product.builder()
                        .name("Product 1")
                        .description("Description 1")
                        .price(100.0)
                        .build()
        );

        this.savedProductId = saved.getId();
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        log.info(">>> Limpiando datos tras el test de integración");
        productRepository.deleteById(savedProductId);

        Path uploadDir = Path.of("uploads/products");
        if (Files.exists(uploadDir)) {
            try (Stream<Path> paths = Files.walk(uploadDir)) {
                paths.filter(Files::isRegularFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                log.error("Error deleting file: {}", file, e);
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
    }

    @Test
    @WithMockUser(username = "testadmin", roles = {"ADMIN"})
    void shouldReturnProductWhenExists() throws Exception {
        log.info(">>> Test: GET /api/v1/products/{}", savedProductId);

        MvcResult result = mockMvc.perform(
                        get("/api/v1/products/" + savedProductId))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        assertNotNull(json);
        assertTrue(json.contains("Product 1"));
        assertTrue(json.contains("Description 1"));
        log.info(">> Product found: {}", json);
    }

    @Test
    @WithMockUser(username = "testadmin", roles = {"ADMIN"})
    void shouldReturnNotFoundWhenProductNotExists() throws Exception {
        log.info(">>> Test: GET /api/v1/products/99999 - esperando 404");

        mockMvc.perform(get("/api/v1/products/99999"))
                .andExpect(status().isNotFound());

        log.info(">> Correctly received 404 error");
    }

    @Test
    @WithMockUser
    void shouldReturnAllProducts() throws Exception {
        log.info(">>> Test: GET /api/v1/products");

        MvcResult result = mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        assertNotNull(json);
        assertTrue(json.contains("Product 1"));
        log.info(">> Products found: {}", json);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldSaveProductAndPersist() throws Exception {
        log.info(">>> Test: POST /api/v1/products");

        MockMultipartFile file = new MockMultipartFile(
                "file", "image.jpeg", "image/jpeg", "image".getBytes()
        );

        MvcResult result = mockMvc.perform(
                multipart(POST, "/api/v1/products")
                        .file(file)
                        .param("name", "Product 2")
                        .param("description", "Description 2")
                        .param("price", "150.00")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
        ).andExpect(status().isCreated()).andReturn();

        String location = result.getResponse().getHeader("Location");
        assertNotNull(location);

        Long generatedId = Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
        assertTrue(productRepository.existsById(generatedId));

        productRepository.deleteById(generatedId);
        log.info(">> Product created with id: {}", generatedId);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldUpdateProduct() throws Exception {
        log.info(">>> Test: PUT /api/v1/products");

        mockMvc.perform(
                put("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": %d,
                                    "name": "Product 1 updated",
                                    "description": "Description 1 updated",
                                    "price": 200.00
                                }
                                """.formatted(savedProductId))
                        .with(csrf())
        ).andExpect(status().isNoContent());

        assertTrue(productRepository.existsById(savedProductId));
        log.info(">> Product updated with id: {}", savedProductId);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void shouldDeleteProduct() throws Exception {
        log.info(">>> Test: DELETE /api/v1/products/{}", savedProductId);

        mockMvc.perform(delete("/api/v1/products/" + savedProductId)
                        .with(csrf()))
                .andExpect(status().isAccepted());

        Thread.sleep(500);
        assertFalse(productRepository.existsById(savedProductId));
        log.info(">> Product deleted with id: {}", savedProductId);
    }
}