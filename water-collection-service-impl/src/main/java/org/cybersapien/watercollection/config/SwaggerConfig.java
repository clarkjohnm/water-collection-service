package org.cybersapien.watercollection.config;

import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration class for Swagger 2
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    /**
     * The Swagger 2 Docket instance
     *
     * @return The Swagger 2 Docket instance
     */
    @Bean
    public Docket api() {
        //noinspection Guava
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                    .apis(RequestHandlerSelectors.any())
                    // Exclude the basic error controller created by default by Spring
                    .paths(Predicates.not(PathSelectors.regex("/error")))
                    .build();
    }

    /**
     * Information about the water collection service
     *
     * @return Information about the water collection service
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Water Collection Service")
                .description("A service to collect water samples")
                .contact(new Contact("John Clark", null, null))
                .license("MIT License")
                .licenseUrl("https://github.com/clarkjohnm/water-collection-service/blob/master/LICENSE.txt")
                .version("1.0")
                .build();
    }

}
