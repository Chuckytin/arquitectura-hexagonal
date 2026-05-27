package com.springboot.web.common.infrastructure.service;

import com.springboot.web.common.infrastructure.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Genera access token con authorities del usuario.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = Map.of("authorities", userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList()
        );
        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Genera token con expiración estándar.
     */
    private String generateToken(Map<String, Object> claims, String subject) {
        return generateToken(claims, subject, jwtProperties.getTokenExpiration());
    }

    /**
     * Construye y firma el token JWT.
     */
    private String generateToken(Map<String, Object> claims, String subject, Long expiration) {
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Parsea y valida token (firma + expiración).
     */
    private Claims getAllClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (IllegalArgumentException e) {
            log.error("Token is null or empty");
            throw new RuntimeException("Token cannot be null or empty", e);
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Parsea token ignorando expiración (útil para renovar).
     */
    private Claims getAllClaimsIgnoringExpiration(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSignInKey())
                    .clockSkewSeconds(Long.MAX_VALUE / 1000)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            log.debug("JWT claim extraction failed (expiration ignored): {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    /**
     * Extrae un claim de un token válido.
     */
    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaims(token));
    }

    /**
     * Extrae un claim ignorando expiración (para renovación).
     */
    private <T> T getClaimIgnoringExpiration(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsIgnoringExpiration(token));
    }

    /**
     * Extrae username incluso de token expirado.
     */
    public String getUsernameFromExpiredToken(String token) {
        return getClaimIgnoringExpiration(token, Claims::getSubject);
    }

    /**
     * Extrae username solo de token válido.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }


    /**
     * Obtiene clave HMAC-SHA de la configuración.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Verifica si el token está expirado.
     */
    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getClaimIgnoringExpiration(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Indica si un token expirado puede renovarse.
     */
    public boolean canBeTokenRenewed(String token) {
        try {
            Date expiration = getClaimIgnoringExpiration(token, Claims::getExpiration);
            Date now = new Date();
            Date refreshWindowStart = new Date(now.getTime() - jwtProperties.getRefreshTokenExpiration());
            return expiration.before(now) && expiration.after(refreshWindowStart);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Genera nuevo access token reutilizando authorities del expirado.
     */
    @SuppressWarnings("unchecked")
    public String renewToken(String token, UserDetails userDetails) {
        if (!canBeTokenRenewed(token)) {
            throw new RuntimeException("Token cannot be renewed - outside refresh window");
        }
        List<String> authorities = getClaimIgnoringExpiration(token,
                claims -> claims.get("authorities", List.class));
        Map<String, Object> claims = Map.of("authorities", authorities);
        return generateToken(claims, userDetails.getUsername());
    }

    /**
     * Valida que el token pertenezca al usuario y no esté expirado ni en blacklist.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            if (tokenBlacklistService.isBlacklisted(token)) {
                log.warn("Token is blacklisted for user: {}", userDetails.getUsername());
                return false;
            }

            final String username = getUsernameFromToken(token);
            return username.equals(userDetails.getUsername());
        } catch (Exception e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Invalida un token añadiéndolo a la blacklist
     */
    public void invalidateToken(String token) {
        try {
            Date expiration = getClaimIgnoringExpiration(token, Claims::getExpiration);
            long timeToLive = expiration.getTime() - System.currentTimeMillis();

            if (timeToLive <= 0) {
                log.debug("Token already expired, no need to blacklist");
                return;
            }

            tokenBlacklistService.invalidateToken(token, timeToLive);
            log.info("Token invalidated successfully");
        } catch (Exception e) {
            log.error("Error invalidating token: {}", e.getMessage());
        }
    }


}