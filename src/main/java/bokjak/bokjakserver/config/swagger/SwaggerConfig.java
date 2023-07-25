package bokjak.bokjakserver.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static bokjak.bokjakserver.common.constant.SwaggerConstants.*;

@OpenAPIDefinition(info = @Info(title = DEFINITION_TITLE, description = DEFINITION_DESCRIPTION, version = DEFINITION_VERSION))
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = SECURITY_SCHEME,
        name = SECURITY_SCHEME_NAME,
        bearerFormat = SECURITY_SCHEME_BEARER_FORMAT,
        description = SECURITY_SCHEME_DESCRIPTION
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi mainAPI() {
        return GroupedOpenApi.builder()
                .group(DEFINITION_TITLE)
                .pathsToMatch(SWAGGER_APPOINTED_PATHS)    // 스웨거로 명세화할 엔드포인트들의 URI 경로
                .build();
    }

}
