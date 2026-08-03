package com.project.uhd.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class GlobalCorsConfig {

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	CorsFilter corsFilter() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);

		config.setAllowedOriginPatterns(Arrays.asList("*"));
		config.setAllowedOrigins(Arrays.asList("http://localhost:25513", "http://localhost:26361", "https://uhd.evergreen-marine.com"));

		config.addAllowedHeader(CorsConfiguration.ALL);
		config.addAllowedMethod(CorsConfiguration.ALL);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/api/**", config); // 所有路徑都做 CORS

		return new CorsFilter(source);
	}
}
