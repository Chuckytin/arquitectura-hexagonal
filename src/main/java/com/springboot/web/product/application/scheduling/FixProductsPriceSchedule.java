package com.springboot.web.product.application.scheduling;

import com.springboot.web.product.domain.port.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixProductsPriceSchedule {

    private final ProductRepository productRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Ejecutar todos los días a las 3:00 AM
    //@Scheduled(fixedRate = 5000) // Ejecutar cada 5 segundos para pruebas
    public void fixProductsPrice() {

        log.info("Fixing products price schedule");

        productRepository.findAll().forEach(product -> {
            product.setPrice(product.getPrice() * 1.1); // Aumentar el precio en un 10%
            productRepository.upsert(product);
        });

        log.info("Fixed products price schedule finished");
    }
}
