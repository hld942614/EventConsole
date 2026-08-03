package com.project.uhd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.project.uhd.handle.AuthenticationFailureHandlerImpl;
import com.project.uhd.handle.AuthenticationSuccessHandlerImpl;
import com.project.uhd.handle.MyAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.formLogin(login -> login.loginProcessingUrl("/login").loginPage("/login")

				.successHandler(new AuthenticationSuccessHandlerImpl())
				.failureHandler(new AuthenticationFailureHandlerImpl()))
				.oauth2Login(oauth2 -> oauth2.loginPage("/login")
						.authorizationEndpoint(authorization -> authorization.baseUri("/oauth2/authorization")
								.authorizationRequestRepository(authorizationRequestRepository()))// 確保 Spring 監聽這個路徑來啟動
																									// OAuth2
						.redirectionEndpoint(redirection -> redirection.baseUri("/login/oauth2/sso")) // 重要：定義處理授權回傳的端點位址
						.defaultSuccessUrl("/", true).failureHandler((request, response, exception) -> {
							logger.warn("OAuth2 fail. {}", exception.getMessage());
							response.sendRedirect(
									"/login?error=" + exception.getMessage().replace("[", "").replace("]", ""));
						}))
				.authorizeHttpRequests(requests -> requests.antMatchers(HttpMethod.POST, "/login").permitAll()
						.antMatchers("/login", "/css/**", "/fonts/**", "/images/**", "/js/**", "/modules/**",
								"/vendor/**", "/*.js", "/actuator/**", "/login/oauth2/**")
						.permitAll().anyRequest().authenticated())
				.logout(logout -> logout.deleteCookies("JSESSIONID").logoutSuccessUrl("/login")
						.logoutRequestMatcher(new AntPathRequestMatcher("/logout")))
				.sessionManagement(session -> session.maximumSessions(1).expiredUrl("/loginpage?expired"))
				.exceptionHandling(handling -> handling.accessDeniedHandler(myAccessDeniedHandler))
				.csrf(csrf -> csrf.disable()).cors(cors -> cors.disable());
		return http.build();
	}

	@Bean
	AuthorizationRequestRepository<OAuth2AuthorizationRequest> authorizationRequestRepository() {
		return new HttpSessionOAuth2AuthorizationRequestRepository();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new AuthenticationSuccessHandlerImpl();
	}

	@Bean
	AuthenticationFailureHandler authenticationFailureHandler() {
		return new AuthenticationFailureHandlerImpl();
	}
}
