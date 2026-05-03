package com.springboot.web;

import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebApplicationTests {

    @MockitoBean
    private QueryProductRepository queryProductRepository;

    @Test
    void contextLoads() {
    }
}