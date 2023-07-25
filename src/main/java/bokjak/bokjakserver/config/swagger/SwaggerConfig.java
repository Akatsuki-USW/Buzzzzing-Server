package bokjak.bokjakserver.config.swagger;

import bokjak.bokjakserver.common.constant.GlobalConstants;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info = @Info(
        title = GlobalConstants.SWAGGER_TITLE,
        description = GlobalConstants.SWAGGER_DESCRIPTION,
        version = GlobalConstants.SWAGGER_VERSION_1))
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi mainAPI() {
        return GroupedOpenApi.builder()
                .group(GlobalConstants.SWAGGER_TITLE)
                .pathsToMatch(GlobalConstants.SWAGGER_PATHS)    // 스웨거로 명세화할 엔드포인트들의 URI 경로
                .build();
    }

}
