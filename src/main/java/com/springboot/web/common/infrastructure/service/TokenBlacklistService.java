package com.springboot.web.common.infrastructure.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class TokenBlacklistService {

    // Almacena tokens invalidados con su tiempo de expiración
    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

    /**
     * Invalida un token añadiéndolo a la blacklist
     */
    public void invalidateToken(String token, long expirationMillis) {
        if (expirationMillis <= 0) {
            log.debug("Token already expired, not adding to blacklist");
            return;
        }
        blacklistedTokens.put(token, System.currentTimeMillis() + expirationMillis);
        log.info("Token invalidated and added to blacklist. Blacklist size: {}",
                blacklistedTokens.size());
    }

    /**
     * Verifica si un token está en la blacklist
     * Si el token ha expirado, lo remueve automáticamente
     */
    public boolean isBlacklisted(String token) {
        Long expiration = blacklistedTokens.get(token);
        if (expiration == null) {
            return false;
        }

        if (System.currentTimeMillis() > expiration) {
            blacklistedTokens.remove(token);
            log.debug("Removed expired token from blacklist");
            return false;
        }

        return true;
    }

    /**
     * Limpia tokens expirados de la blacklist
     * Se ejecuta cada refresh-token-expiration milisegundos
     */
    @Scheduled(fixedDelayString = "${jwt.refresh-token-expiration:300000}")
    public void cleanExpiredTokens() {
        long now = System.currentTimeMillis();
        int removedCount = 0;

        var iterator = blacklistedTokens.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (now > entry.getValue()) {
                iterator.remove();
                removedCount++;
            }
        }

        if (removedCount > 0) {
            log.info("Cleaned {} expired tokens from blacklist. Remaining: {}",
                    removedCount, blacklistedTokens.size());
        }
    }
    
}