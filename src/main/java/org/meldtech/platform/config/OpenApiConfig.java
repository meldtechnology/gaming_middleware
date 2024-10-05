package org.meldtech.platform.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${springdoc.server-url}")
    private String serverUrl;
    List<Tag> tags = new ArrayList<>();

    @Bean
    public OpenAPI customOpenAPI() {

        Server server = new Server();
        server.setUrl(serverUrl);

        return new OpenAPI()
                .servers(List.of(server))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .in(In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addParameters("accept", createHeader("accept", "*/*"))
                        .addParameters("Content-Type", createHeader("Content-Type", "application/json")))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    @Bean
    public OpenApiCustomiser globalHeaderOpenApiCustomiser() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/accept"));
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/Content-Type"));
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/x-country-code"));
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/x-tenant-id"));
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/x-trace-id"));
            operation.addParametersItem(new Parameter().$ref("#/components/parameters/x-created-by"));
        }));
    }

    @Bean
    org.springdoc.core.models.GroupedOpenApi authorityApis() { // group all APIs with `admin` in the path
        return org.springdoc.core.models.GroupedOpenApi.builder().group("Authorization Settings").pathsToMatch("/**/oauth2/**").build();
    }

    @Bean
    org.springdoc.core.models.GroupedOpenApi roleApis() { // group all APIs with `admin` in the path
        return org.springdoc.core.models.GroupedOpenApi.builder().group("Application Roles").pathsToMatch("/**/roles/**").build();
    }

    @Bean
    org.springdoc.core.models.GroupedOpenApi usersApis() { // group all APIs with `admin` in the path
        return org.springdoc.core.models.GroupedOpenApi.builder().group("App Users").pathsToMatch("/**/users/**").build();
    }

    @Bean
    org.springdoc.core.models.GroupedOpenApi documentsApis() { // group all APIs with `admin` in the path
        return org.springdoc.core.models.GroupedOpenApi.builder().group("Documents").pathsToMatch("/**/documents/**").build();
    }


    private Parameter createHeader(String name, String defaultValue) {
        return new Parameter()
                .in("header")
                .schema(new StringSchema()._default(defaultValue))
                .name(name)
                .required(false);
    }


}
