package pw.react.backend.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    @Profile({"!jwt"})
    public OpenAPI openAPI(@Value("${application.description}") String appDescription,
                           @Value("${application.version}") String appVersion) {
        return createOpenApi(appDescription, appVersion);
    }

    private OpenAPI createOpenApi(String appDescription, String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title("Backend PW API")
                        .version(appVersion)
                        .description(appDescription)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    @Profile({"jwt"})
    public OpenAPI jwtOpenAPI(@Value("${application.description}") String appDescription,
                              @Value("${application.version}") String appVersion) {
        final String securitySchemeName = "bearerAuth";
        return createOpenApi(appDescription, appVersion)
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }

}
