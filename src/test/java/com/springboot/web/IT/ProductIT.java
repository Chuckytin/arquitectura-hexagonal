package com.springboot.web.IT;

import com.springboot.web.IT.config.TestSecurityConfig;
import com.springboot.web.common.domain.PaginationResult;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import com.springboot.web.product.infrastructure.api.dto.ProductDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración para el controlador de productos.
 * Estas pruebas arrancan el contexto completo de Spring Boot y prueban los endpoints REST utilizando RestTemplate y MockMvc.
 * Se asegura de que los datos se preparen antes de cada prueba y se limpien después para mantener la independencia de las pruebas.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@ActiveProfiles("test")
@Slf4j
class ProductIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private RestTemplate restTemplate;
    private Long savedProductId;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        restTemplate = new RestTemplate();

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

    private HttpEntity<String> createAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testuser", "testpass");
        return new HttpEntity<>(headers);
    }

    @Test
    void shouldReturnProductWhenExists() {
        log.info(">>> Test: GET /api/v1/products/{}", savedProductId);

        ResponseEntity<ProductDto> response = restTemplate.exchange(
                baseUrl + "/api/v1/products/" + savedProductId,
                HttpMethod.GET,
                createAuthEntity(),
                ProductDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Product 1", response.getBody().getName());
        assertEquals("Description 1", response.getBody().getDescription());
        assertEquals(100.0, response.getBody().getPrice());
    }

    @Test
    void shouldReturnNotFoundWhenProductNotExists() {
        log.info(">>> Test: GET /api/v1/products/99999 - esperando 404");

        try {
            restTemplate.exchange(
                    baseUrl + "/api/v1/products/99999",
                    HttpMethod.GET,
                    createAuthEntity(),
                    ProductDto.class);
            fail("Should have thrown HttpClientErrorException.NotFound");
        } catch (HttpClientErrorException.NotFound e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatusCode());
            log.info("✅ Correctly received 404 error");
        }
    }

    @Test
    void shouldReturnAllProducts() {
        log.info(">>> Test: GET /api/v1/products");

        // Usar ParameterizedTypeReference para manejar la respuesta con paginación
        ParameterizedTypeReference<PaginationResult<ProductDto>> responseType =
                new ParameterizedTypeReference<>() {
                };

        ResponseEntity<PaginationResult<ProductDto>> response = restTemplate.exchange(
                baseUrl + "/api/v1/products",
                HttpMethod.GET,
                createAuthEntity(),
                responseType);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        PaginationResult<ProductDto> paginationResult = response.getBody();
        assertNotNull(paginationResult.content());
        assertFalse(paginationResult.content().isEmpty());
        assertEquals(0, paginationResult.page());
        assertTrue(paginationResult.totalElements() >= 1);

        log.info(">>> Total elements: {}", paginationResult.totalElements());
        log.info(">>> Content size: {}", paginationResult.content().size());
    }

    @Test
    @WithMockUser
    void shouldSaveProductAndPersist() throws Exception {
        log.info(">>> Test: POST /api/v1/products");

        MockMultipartFile file = new MockMultipartFile(
                "file", "image.jpeg", "image/jpeg", "image".getBytes()
        );

        MvcResult result = mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/products")
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

        // Limpiar producto creado en la prueba
        productRepository.deleteById(generatedId);
    }

    @Test
    @WithMockUser
    void shouldUpdateProduct() throws Exception {
        log.info(">>> Test: PUT /api/v1/products");

        MockMultipartFile file = new MockMultipartFile(
                "file", "image.jpeg", "image/jpeg", "image".getBytes()
        );

        mockMvc.perform(
                multipart(HttpMethod.PUT, "/api/v1/products")
                        .file(file)
                        .param("id", savedProductId.toString())
                        .param("name", "Product 1 updated")
                        .param("description", "Description 1 updated")
                        .param("price", "200.00")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
        ).andExpect(status().isNoContent());

        assertTrue(productRepository.existsById(savedProductId));
    }

    @Test
    @WithMockUser
    void shouldDeleteProduct() throws Exception {
        log.info(">>> Test: DELETE /api/v1/products/{}", savedProductId);

        mockMvc.perform(delete("/api/v1/products/" + savedProductId)
                        .with(csrf()))
                .andExpect(status().isAccepted());

        Thread.sleep(500);
        assertFalse(productRepository.existsById(savedProductId));
    }
}