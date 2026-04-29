package com.springboot.web.IT.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuración de seguridad para pruebas de integración.
 * Define un usuario en memoria para autenticación durante las pruebas.
 */
@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        UserDetails user = User.withUsername("testuser")
                .password("{noop}testpass") // {noop} significa sin encriptación
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
}