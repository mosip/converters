package io.mosip.kernel.bio.converter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {
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
	
	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}
}