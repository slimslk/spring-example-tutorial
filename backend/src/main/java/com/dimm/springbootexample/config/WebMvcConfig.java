package com.dimm.springbootexample.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("#{'${cors.allowed-origins}'.split(',')}")
	private List<String> allowedOrigins;
	@Value("#{'${cors.allowed-methods}'.split(',')}")
	private List<String> allowedMethods;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		allowedMethods.forEach(System.out::println);
		registry.addMapping("/api/**")
				.allowedOrigins(allowedOrigins.toArray(String[]::new))
				.allowedMethods(allowedMethods.toArray(String[]::new));
	}
}
