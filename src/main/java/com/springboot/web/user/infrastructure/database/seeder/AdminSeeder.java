package com.springboot.web.user.infrastructure.database.seeder;

import com.springboot.web.user.domain.entity.User;
import com.springboot.web.user.domain.entity.UserRole;
import com.springboot.web.user.domain.port.PasswordEncoderPort;
import com.springboot.web.user.domain.port.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "local", "prod"})
@Order(0)
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String @NonNull ... args) {

        try {
            if (userRepository.findByEmail(adminEmail).isPresent()) {
                log.info("Seeder omitido: el usuario admin ya existe.");
                return;
            }

            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoderPort.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .build();

            userRepository.upsert(admin);
            log.info("Seeder completado: usuario admin creado con email {}.", adminEmail);
            
        } catch (Exception e) {
            log.error("Seeder falló — la tabla users puede no existir aún: {}", e.getMessage());
            throw e;
        }

    }

}