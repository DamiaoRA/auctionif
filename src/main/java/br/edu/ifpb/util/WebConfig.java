package br.edu.ifpb.util;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")  // Permite CORS para qualquer endpoint que comece com "/api/"
                .allowedOrigins("http://localhost:5173");  // Permite requisições apenas deste domínio
    }
}
