package io.mosip.kernel.bio.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

/**
 * Configuration class for setting up security configurations for the application.
 * Disables HTTP basic authentication and CSRF protection, and permits all requests.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain.
     * Disables HTTP basic authentication and CSRF protection, and allows all requests.
     *
     * @param httpSecurity the {@link HttpSecurity} to modify
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        /*
         *  Disabling CSRF protection because this is a stateless API that uses token-based authentication
         */
		httpSecurity.csrf(AbstractHttpConfigurer::disable); // NOSONAR
		httpSecurity.authorizeHttpRequests(http -> http.anyRequest().permitAll());
		return httpSecurity.build();
	}
	
	/**
     * Provides an {@link AuthenticationEntryPoint} that returns a 401 Unauthorized response for unauthenticated requests.
     *
     * @return the {@link AuthenticationEntryPoint}
     */
	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}