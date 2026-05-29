package com.springboot.web.common.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.web.common.infrastructure.exceptions.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 401 - No autenticado
 * Maneja diferentes estrategias según el tipo de petición:
 * - API REST (con JWT): Devuelve JSON
 * - OAuth2/Form login: Redirige al login
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * Verifica si es una petición de API REST
     * Verifica si es OAuth2 o form login
     * Para APIs REST devuelve JSON 401
     * Para OAuth2, form login o navegador redirige al login de OAuth2
     * Guarda la URL original para redirigir después del login
     * Redirige a login
     */
    @Override
    public void commence(HttpServletRequest request,
                         @NonNull HttpServletResponse response,
                         @NonNull AuthenticationException exception) throws IOException {

        String requestURI = request.getRequestURI();
        String acceptHeader = request.getHeader("Accept");

        log.debug("Authentication failed for URI: {}, Accept: {}", requestURI, acceptHeader);

        boolean isActuatorRequest = requestURI.startsWith("/actuator");

        boolean isApiRequest = requestURI.startsWith("/api/") &&
                (acceptHeader != null && acceptHeader.contains("application/json"));

        boolean isOAuth2Request = requestURI.contains("/oauth2/") ||
                requestURI.contains("/login");

        if ((isApiRequest || isActuatorRequest) && !isOAuth2Request) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            objectMapper.writeValue(response.getWriter(), new ErrorMessage(
                    exception.getMessage() != null ? exception.getMessage() : "Full authentication is required",
                    exception.getClass().getName(),
                    requestURI
            ));
        } else {
            log.debug("Redirecting to OAuth2 login for URI: {}", requestURI);

            request.getSession().setAttribute("REDIRECT_URL", requestURI);

            response.sendRedirect("/login");
        }
    }
}