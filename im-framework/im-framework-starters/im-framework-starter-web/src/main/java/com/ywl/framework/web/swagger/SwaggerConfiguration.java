package com.ywl.framework.web.swagger;

import com.ywl.framework.common.model.IDict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Swagger配置
 *
 * @author lgy
 */
@Slf4j
@Configuration
@EnableOpenApi
public class SwaggerConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(applicationName)
                .description(applicationName + "接口文档。")
                .contact(new Contact("严选项目组", "", ""))
                .version("1.0")
                .build();
    }

    @Bean
    public EnumPropertyBuilderPlugin enumPropertyBuilderPlugin() {
        return new EnumPropertyBuilderPlugin();
    }


    @Bean
    public ValidatePropertyBuilderPlugin validatePropertyBuilderPlugin() {
        return new ValidatePropertyBuilderPlugin();
    }
}
