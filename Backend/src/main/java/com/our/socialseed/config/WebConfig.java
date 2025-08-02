package com.our.socialseed.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta para evitar errores si el proyecto se ejecuta desde otro directorio
        String userUploadDir = Paths.get("uploads/public/").toAbsolutePath().toUri().toString();

        // Permite acceder a imágenes subidas por el usuario desde /public/**
        registry.addResourceHandler("/public/**")
                .addResourceLocations(userUploadDir)
                .setCachePeriod(0); // Desactiva cache para que los cambios sean visibles al instante

        // Sirve archivos estáticos del proyecto desde /assets/**
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/public/");
    }
}