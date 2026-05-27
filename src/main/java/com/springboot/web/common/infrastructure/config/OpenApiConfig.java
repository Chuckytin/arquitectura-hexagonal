package com.springboot.web.common.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@OpenAPIDefinition(
        info = @Info(
                title = "${api.docs.title}",
                version = "${api.docs.version}",
                description = "${api.docs.description}",
                contact = @Contact(
                        name = "${api.docs.contact.name}",
                        email = "${api.docs.contact.email}",
                        url = "${api.docs.contact.url}"
                ),
                license = @License(
                        name = "${api.docs.license.name}",
                        url = "${api.docs.license.url}"
                )
        ),
        servers = {
                @Server(
                        url = "${api.docs.servers.url}",
                        description = "${api.docs.servers.description}"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = "JWT authentication token. This introduces the token without the 'Bearer ' prefix."
)
@Configuration
@Profile({"dev", "local"})
public class OpenApiConfig {
}
