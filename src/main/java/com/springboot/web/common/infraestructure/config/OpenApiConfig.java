package com.springboot.web.common.infraestructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
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
        }
)
@Configuration
@Profile({"dev", "local"})
public class OpenApiConfig {
}
