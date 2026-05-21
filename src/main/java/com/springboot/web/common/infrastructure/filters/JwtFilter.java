package com.springboot.web.common.infrastructure.filters;

import com.springboot.web.common.infrastructure.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta cada petición HTTP exactamente una vez (OncePerRequestFilter).
 * Responsable de extraer el token del header Authorization, renovarlo si está expirado
 * pero dentro de la ventana de renovación, validarlo y establecer la autenticación
 * en el SecurityContext para que Spring Security autorice la petición.
 * 1. Si no hay token Bearer, continúa sin autenticar (endpoints públicos)
 * 2. Extrae username del token tolerando expiración
 * 3. Si está expirado y renovable, renueva y devuelve nuevo token en header X-Token-Renewed
 * 4. Valida el token (original o renovado), establece autenticación en SecurityContext
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            log.debug("No Bearer token found, continuing filter chain");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7);
        log.debug("Token received successfully");

        try {

            String username = jwtService.getUsernameFromExpiredToken(token);
            log.debug("Extracted username: {}", username);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // para cargar el usuario real con sus roles desde base de datos
                UserDetails userDetails = User.withDefaultPasswordEncoder()
                        .username(username)
                        .password("password")
                        .roles("USER")
                        .build();

                if (jwtService.isTokenExpired(token) && jwtService.canBeTokenRenewed(token)) {
                    token = jwtService.renewToken(token, userDetails);
                    response.setHeader("X-Token-Renewed", "Bearer " + token);
                    log.info("Token renewed for user: {}", username);
                }

                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    log.debug("Authentication successful for user: {}", username);
                    
                } else {
                    log.warn("Invalid token for user: {}", username);
                }
            }

        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}