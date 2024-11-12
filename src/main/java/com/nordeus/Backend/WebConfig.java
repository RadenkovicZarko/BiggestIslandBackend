package com.nordeus.Backend;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**") // Allow CORS on all endpoints
      .allowedOrigins("http://localhost:3000") // Allow your React frontend
      .allowedMethods("GET", "POST", "PUT", "DELETE") // Allow necessary methods
      .allowCredentials(true);
  }
}
