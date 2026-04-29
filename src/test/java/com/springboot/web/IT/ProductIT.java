package com.springboot.web.IT;

import com.springboot.web.IT.config.TestSecurityConfig;
import com.springboot.web.product.domain.entity.Product;
import com.springboot.web.product.domain.port.ProductRepository;
import com.springboot.web.product.infraestructure.api.dto.ProductDto;
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
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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
 * Estas pruebas verifican el comportamiento de los endpoints REST para la gestión de productos, incluyendo la creación, actualización, eliminación y consulta de productos.
 * Se utiliza un RestTemplate para realizar solicitudes HTTP reales al servidor de pruebas, y MockMvc para pruebas más específicas de los endpoints.
 * La base de datos se prepara antes de cada prueba y se limpia después para asegurar que las pruebas sean independientes y reproducibles.
 * Se incluye una configuración de seguridad de prueba para autenticar las solicitudes a los endpoints protegidos durante las pruebas de integración.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
@Slf4j
class ProductIT {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    private final RestTemplate restTemplate;

    String baseUrl;

    public ProductIT() {
        this.restTemplate = new RestTemplate();
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        log.info(">>> Preparando datos para el test de integración");
        productRepository.upsert(
                Product.builder()
                        .id(1L).name("Product 1").description("Description 1").price(100.0)
                        .build()
        );
    }

    @SneakyThrows
    @AfterEach
    void tearDown() {
        log.info(">>> Limpiando datos tras el test de integración");
        productRepository.deleteById(1L);
        productRepository.deleteById(2L);

        Path uploadDir = Path.of("uploads/products");

        if (Files.exists(uploadDir)) {
            try (Stream<Path> paths = Files.walk(uploadDir)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(file -> {
                            try {
                                Files.delete(file);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        }
    }

    /**
     * Crea una entidad HTTP con las credenciales de autenticación básica para el usuario "testuser" con contraseña "testpass".
     * Se utiliza para autenticar las solicitudes a los endpoints protegidos durante las pruebas de integración.
     */
    private HttpEntity<String> createAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("testuser", "testpass");
        return new HttpEntity<>(headers);
    }

    @Test
    void shouldReturnProductWhenExists() {
        log.info(">>> Test: GET /api/v1/products/1");

        ResponseEntity<ProductDto> response = restTemplate.exchange(
                baseUrl + "/api/v1/products/1",
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
        log.info(">>> Test: GET /api/v1/products/99 - esperando 404");

        try {
            ResponseEntity<ProductDto> response = restTemplate.exchange(
                    baseUrl + "/api/v1/products/99",
                    HttpMethod.GET,
                    createAuthEntity(),
                    ProductDto.class);
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("404") || e.getMessage().contains("Not Found"));
        }
    }

    @Test
    void shouldReturnAllProducts() {
        log.info(">>> Test: GET /api/v1/products");

        ResponseEntity<ProductDto[]> response = restTemplate.exchange(
                baseUrl + "/api/v1/products",
                HttpMethod.GET,
                createAuthEntity(),
                ProductDto[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().length >= 1);
    }

    /**
     * Se utiliza MockMvc para simular la solicitud multipart/form-data con el archivo de imagen y los parámetros del producto, y se deshabilita la protección CSRF para esta prueba.
     */
    @Test
    @WithMockUser
    void shouldSaveProductAndPersist() throws Exception {
        log.info(">>> Test: POST /api/v1/products");

        MockMultipartFile file = new MockMultipartFile(
                "file", "image.jpeg", "image/jpeg", "image".getBytes()
        );

        mockMvc.perform(
                multipart(HttpMethod.POST, "/api/v1/products")
                        .file(file)
                        .param("id", "2")
                        .param("name", "Product 2")
                        .param("description", "Description 2")
                        .param("price", "150.00")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf()) // deshabilita la protección CSRF
        ).andExpect(status().isCreated());

        assertTrue(productRepository.existsById(2L));
    }

    /**
     * Se utiliza MockMvc para simular la solicitud multipart/form-data con el archivo de imagen y los parámetros del producto, y se deshabilita la protección CSRF para esta prueba.
     */
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
                        .param("id", "1")
                        .param("name", "Product 1 updated")
                        .param("description", "Description 1 updated")
                        .param("price", "200.00")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())
        ).andExpect(status().isNoContent());

        assertTrue(productRepository.existsById(1L));
    }

    /**
     * Se utiliza MockMvc para simular la solicitud DELETE al endpoint de eliminación de productos, y se deshabilita la protección CSRF para esta prueba.
     */
    @Test
    @WithMockUser
    void shouldDeleteProduct() throws Exception {
        log.info(">>> Test: DELETE /api/v1/products/1");

        mockMvc.perform(delete("/api/v1/products/1")
                        .with(csrf()))
                .andExpect(status().isAccepted());

        Thread.sleep(500);
        assertFalse(productRepository.existsById(1L));
    }
}