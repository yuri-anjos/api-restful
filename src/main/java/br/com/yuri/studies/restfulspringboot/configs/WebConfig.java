package br.com.yuri.studies.restfulspringboot.configs;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  private static final MediaType MEDIA_TYPE_APPLICATION_YAML =
      MediaType.valueOf("application/x-yaml");

  @Value("${cors.originPatterns}")
  private String corsOriginPatterns;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    var allowedOrigins = corsOriginPatterns.split(",");
    registry
        .addMapping("/**")
        .allowedMethods("*")
        .allowedOrigins(allowedOrigins)
        .allowCredentials(Boolean.TRUE);
  }

  @Override
  public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(new YamlHttpConverter());
  }

  @Override
  public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
    configurer
        .favorParameter(Boolean.FALSE)
        .ignoreAcceptHeader(Boolean.FALSE)
        .useRegisteredExtensionsOnly(Boolean.FALSE)
        .defaultContentType(MediaType.APPLICATION_JSON)
        .mediaType("json", MediaType.APPLICATION_JSON)
        .mediaType("xml", MediaType.APPLICATION_XML)
        .mediaType("x-yaml", MEDIA_TYPE_APPLICATION_YAML);
  }
}
