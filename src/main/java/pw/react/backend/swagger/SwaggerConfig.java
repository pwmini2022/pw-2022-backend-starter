package pw.react.backend.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Configuration
public class SwaggerConfig {

    @Value("${swagger.contact.name}")
    private String contactName;
    @Value("${swagger.contact.email}")
    private String contactEmail;

    private static final Set<String> DEFAULT_PRODUCES = new HashSet<>(Arrays.asList(
            "application/json", "application/xml"
    ));
    private static final Set<String> DEFAULT_CONSUMES = new HashSet<>(Arrays.asList(
            "application/json", "application/xml"
    ));

    private ApiKey apiKey() {
        return new ApiKey("JWT", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return List.of(new SecurityReference("JWT", authorizationScopes));
    }

    @Bean
    public Docket api() {
        Contact defaultContact = new Contact(contactName, "", contactEmail);
        ApiInfo apiInfo = new ApiInfo(
                "PW2021 API",
                "API description for the PW 2021 backend semester project.",
                "1.0",
                "urn:tos",
                defaultContact,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                List.of()
        );

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .produces(DEFAULT_PRODUCES)
                .consumes(DEFAULT_CONSUMES)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(apiKey()))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }
}
