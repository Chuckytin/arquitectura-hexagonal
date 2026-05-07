package com.springboot.web.product.application.scheduling;

import com.springboot.web.product.infraestructure.database.repository.QueryProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FixProductsPriceSchedule {

    private final QueryProductRepository queryProductRepository;

    @Scheduled(cron = "0 0 3 * * ?") // Ejecutar todos los días a las 3:00 AM
    //@Scheduled(fixedRate = 5000) // Ejecutar cada 5 segundos para pruebas
    public void fixProductsPrice() {
        log.info("Fixing products price schedule");

        int updatedCount = queryProductRepository.updateAllPricesByPercent(1.1); // Aumenta 10%

        log.info("Fixed products price schedule finished. Updated {} products", updatedCount);
    }
}
