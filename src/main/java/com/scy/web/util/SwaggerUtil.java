package com.scy.web.util;

import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * SwaggerUtil
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/19.
 */
public class SwaggerUtil {

    private SwaggerUtil() {
    }

    public static Docket getDocket(
            String title,
            String description,
            String version,
            String basePackage
    ) {
        ApiInfo apiInfo = new ApiInfoBuilder().title(title).description(description).version(version).build();

        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(Boolean.FALSE)
                .genericModelSubstitutes(ResponseEntity.class)
                .forCodeGeneration(Boolean.TRUE)
                // 项目名称
                .pathMapping("/")
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage(basePackage))
                .build();
    }
}
