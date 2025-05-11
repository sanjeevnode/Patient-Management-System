package com.sanjeevnode.patientservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI defineOpenApi() {
        Server server = new Server();
        server.setUrl("http://localhost:4000");
        server.setDescription("Development");

        Contact myContact = new Contact();
        myContact.setName("Sanjeev Singh");
        myContact.setEmail("me.sanjeevks@gmail.com");

        Info information = new Info()
                .title("Patient Service API")
                .version("1.0")
                .description("API for managing patient records")
                .contact(myContact);
        return new OpenAPI().info(information).servers(List.of(server));
    }
}