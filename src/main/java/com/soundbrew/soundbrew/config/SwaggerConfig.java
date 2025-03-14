package com.soundbrew.soundbrew.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@OpenAPIDefinition(
        info = @Info(title = "API Documentation", version = "1.0"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)


/*
Authorization 헤더(BearerAuth)를 전역 추가
 */
public class SwaggerConfig {

    //  특정 메서드 전역헤더 제외 나머지는 추가
    @Bean
    public OperationCustomizer customizeOperation() {
        return (operation, handlerMethod) -> {
            // 특정 엔드포인트 제외 조건 설정
            if (handlerMethod.getMethod().getName().equals("excludeHeader")) {
                // 특정 메서드에서 헤더를 제외할 경우
                operation.setParameters(null); // 모든 파라미터 제거
            } else {
                // 전역 헤더를 추가
                operation.addParametersItem(
                        new HeaderParameter()
                                .required(false)
                                .schema(new StringSchema())
                                .description("헤더 파라미터 설명")
                                .name("headerParam1")
                );
            }
            return operation;
        };
    }
}
