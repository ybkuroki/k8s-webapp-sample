package managesys.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@ConditionalOnProperty(prefix = "extension.docs.restapi", name = "enabled", matchIfMissing = false)
public class SpringFoxSwaggerConfig {

    @Bean
    public Docket document() {
        return new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("managesys.controller"))
                    .paths(paths())
                    .build()
                    .apiInfo(apiInfo());
    }

    private Predicate<String> paths() {
        return PathSelectors.any();
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfoBuilder().title("springboot-webapp-sample WebAPI")
                                                .description("web api specifications")
                                                .version("1.0")
                                                .license("MIT License")
                                                .licenseUrl("https://opensource.org/licenses/MIT")
                                                .build();
        return apiInfo;
    }
}
