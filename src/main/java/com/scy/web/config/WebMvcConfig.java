package com.scy.web.config;

import com.scy.core.SystemUtil;
import com.scy.core.format.MessageUtil;
import com.scy.core.json.JsonUtil;
import com.scy.core.rest.ResponseResult;
import com.scy.web.util.ExceptionHandlerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * WebMvcConfig
 *
 * @author shichunyang
 * Created by shichunyang on 2020/9/20.
 */
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseTrailingSlashMatch(Boolean.FALSE);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String fe = "file:/data/webapps/";
        registry.addResourceHandler("/**").addResourceLocations(fe);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(CorsConfiguration.ALL)
                .allowedMethods(CorsConfiguration.ALL)
                .allowedHeaders(CorsConfiguration.ALL)
                .allowCredentials(Boolean.TRUE);
    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> handlerExceptionResolvers) {
        handlerExceptionResolvers.clear();

        handlerExceptionResolvers.add((request, response, handler, exception) -> {
            response.setCharacterEncoding(SystemUtil.CHARSET_UTF_8_STR);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            try (OutputStream outputStream = response.getOutputStream()) {
                ResponseResult<?> responseResult = ExceptionHandlerUtil.exception2ResponseResult(request, exception);
                outputStream.write(JsonUtil.object2Json(responseResult).getBytes());
                outputStream.flush();
            } catch (IOException e) {
                log.warn(MessageUtil.format("extendHandlerExceptionResolvers io error", e));
            }

            return new ModelAndView();
        });
    }
}
