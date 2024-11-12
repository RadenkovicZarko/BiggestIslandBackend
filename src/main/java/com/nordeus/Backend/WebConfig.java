package com.nordeus.Backend;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
      .allowedOrigins("https://your-netlify-app-url.com") // Replace with your actual Netlify URL
      .allowedMethods("GET", "POST", "PUT", "DELETE")
      .allowedHeaders("*");
  }
}
