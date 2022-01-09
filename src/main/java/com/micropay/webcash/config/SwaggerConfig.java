package com.micropay.webcash.config;

import com.fasterxml.classmate.TypeResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static java.util.Collections.singletonList;

@Configuration
public class SwaggerConfig {

    @Autowired
    private TypeResolver typeResolver;

    @Bean
    public Docket customerReference() {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("webcash-apis Reference")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.micropay.webcash.controller"))
                .paths(PathSelectors.any())
                .build()
                .securitySchemes(singletonList(apiKey()));
    }

    private ApiInfo apiInfo() {
        Contact contactInfo = new Contact("Support Services Contact",
                "https://webcash-apis-apis.com/contact-us/",
                "support@webcash-apis-apis.com");

        return new ApiInfoBuilder().title("Webcash Services")
                .description("Webcash API reference for developers")
                .termsOfServiceUrl("https://webcash-apis-apis.com/webcash")
                .contact(contactInfo).license("Webcash License")
                .licenseUrl(contactInfo.getUrl()).version("1.0").build();
    }

    private ApiKey apiKey() {
        return new ApiKey("apiKey",
                "Authorization", "header");
    }
}
