package com.kinart.portail.business.utils;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ListableBeanFactory beanFactory;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        Map<String, Object> components = beanFactory.getBeansWithAnnotation(RequestParameterConverter.class);
        components.values().parallelStream().forEach(c -> {
            if(c instanceof Converter) {
                registry.addConverter((Converter)c);
            }
        });
    }

    /*@Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
                .allowedOrigins("http://localhost:4200", "http://localhost:8081", "http://192.168.160.224:8080", "http://192.168.160.224:8081")
                .allowedMethods("*")
                .maxAge(3600L)
                .allowedHeaders("*")
                .exposedHeaders("Authorization")
                .allowCredentials(true);
    }*/
}
