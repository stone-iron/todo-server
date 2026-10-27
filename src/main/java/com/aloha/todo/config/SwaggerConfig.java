package com.aloha.todo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI todoOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("TodoList Project API")
                .description("할 일 목록(TodoList) 백엔드 API 입니다.")
                .version("v0.0.1"));
    }
}
