package io.mosip.kernel.bio.converter.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

<<<<<<< HEAD
=======
import jakarta.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
>>>>>>> 5ba3193 (MOSIP-32456)
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
<<<<<<< HEAD
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
=======
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
>>>>>>> 5ba3193 (MOSIP-32456)
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
<<<<<<< HEAD
=======
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
>>>>>>> 5ba3193 (MOSIP-32456)
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
<<<<<<< HEAD

import com.fasterxml.jackson.module.afterburner.AfterburnerModule;

/**
 * Configuration class for setting up security, CORS, and MVC configurations.
 * 
 * <p>
 * This class provides various beans and configurations for:
 * </p>
 * <ul>
 * <li>HttpFirewall customization to prevent security threats.</li>
 * <li>Web security customization for allowing certain endpoints.</li>
 * <li>CORS configuration allowing all origins, methods, and headers.</li>
 * <li>AuthenticationManager setup for authentication handling.</li>
 * <li>In-memory user details service for authentication purposes.</li>
 * <li>AfterburnerModule setup for optimizing JSON serialization.</li>
 * <li>Password encoder for encoding user passwords securely.</li>
 * </ul>
 * 
 * <p>
 * It also includes specific configurations for Spring Security, CORS, and
 * method security.
 * </p>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
=======
>>>>>>> 5ba3193 (MOSIP-32456)

@Configuration
@EnableWebSecurity
@EnableWebMvc
<<<<<<< HEAD
<<<<<<< HEAD
@EnableMethodSecurity(prePostEnabled = true)
@Order(2)
public class TestSecurityConfig {

	/**
	 * Customizes the HttpFirewall to use DefaultHttpFirewall.
	 * 
	 * @return DefaultHttpFirewall instance
	 */
=======
//@EnableMethodSecurity
@EnableMethodSecurity(prePostEnabled = true// , securedEnabled = true, jsr250Enabled = true
)
=======
@EnableMethodSecurity(prePostEnabled = true)
>>>>>>> 3ebf8d7 ([MOSIP-33316])
@Order(2)
public class TestSecurityConfig {

>>>>>>> 5ba3193 (MOSIP-32456)
	@Bean
	public HttpFirewall defaultHttpFirewall() {
		return new DefaultHttpFirewall();
	}

<<<<<<< HEAD
	/**
	 * Customizes web security to ignore specific endpoints and use default
	 * HttpFirewall.
	 * 
	 * @return WebSecurityCustomizer instance
	 */
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(allowedEndPoints()).and().httpFirewall(defaultHttpFirewall());
	}

	/**
	 * Defines allowed endpoints for HTTP requests.
	 * 
	 * @return Array of allowed endpoint patterns
	 */
=======
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return web -> web.ignoring().requestMatchers(allowedEndPoints()).and().httpFirewall(defaultHttpFirewall());
	}

>>>>>>> 5ba3193 (MOSIP-32456)
	private String[] allowedEndPoints() {
		return new String[] { "/assets/**", "/icons/**", "/screenshots/**", "/favicon**", "/**/favicon**", "/css/**",
				"/js/**", "/*/error**", "/*/webjars/**", "/*/v2/api-docs", "/*/configuration/ui",
				"/*/configuration/security", "/*/swagger-resources/**", "/*/swagger-ui.html", "/**/authenticate/**" };
	}

<<<<<<< HEAD
	/**
	 * Configures CORS support for all origins, methods, and headers.
	 * 
	 * @return CorsConfigurationSource instance
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		/*
		 * or simply "*"
		 */
		configuration.setAllowedMethods(Arrays.asList("*", "POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	/**
	 * Configures the AuthenticationManager bean.
	 * 
	 * @param authenticationConfiguration AuthenticationConfiguration instance
	 * @return AuthenticationManager instance
	 * @throws Exception if an error occurs while retrieving AuthenticationManager
	 */
	/*
	 * @Bean public AuthenticationManager
	 * authenticationManager(AuthenticationConfiguration
	 * authenticationConfiguration) throws Exception { return
	 * authenticationConfiguration.getAuthenticationManager(); }
	 */
	/**
	 * Configures an in-memory user details service with predefined users and roles.
	 * 
	 * @return InMemoryUserDetailsManager instance
	 */
	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		List<UserDetails> users = new ArrayList<>();
		users.add(new User("reg-officer", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_OFFICER"))));
		users.add(new User("reg-supervisor", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_SUPERVISOR"))));
		users.add(new User("reg-admin", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_ADMIN"))));
		users.add(new User("reg-processor", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_PROCESSOR"))));
		users.add(new User("id-auth", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_ID_AUTHENTICATION"))));
		users.add(new User("individual", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_INDIVIDUAL"))));
		users.add(new User("test", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_TEST"))));
		return new InMemoryUserDetailsManager(users);
	}

	/**
	 * Configures the AfterburnerModule for optimizing JSON serialization.
	 * 
	 * @return AfterburnerModule instance
	 */
	@Bean
	public AfterburnerModule afterburnerModule() {
		return new AfterburnerModule();
	}

	/**
	 * Configures CORS globally for all controllers and methods.
	 * 
	 * @return WebMvcConfigurer instance
	 */
=======
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		/* 
		 * or simply "*"
		 */
		configuration.setAllowedMethods(Arrays.asList("*", "POST", "PUT", "GET", "OPTIONS", "DELETE", "PATCH")); 
		configuration.setAllowedHeaders(Arrays.asList("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public InMemoryUserDetailsManager userDetailsService() {
		List<UserDetails> users = new ArrayList<>();
		users.add(new User("reg-officer", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_OFFICER"))));
		users.add(new User("reg-supervisor", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_SUPERVISOR"))));
		users.add(new User("reg-admin", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_ADMIN"))));
		users.add(new User("reg-processor", "mosip",
				Arrays.asList(new SimpleGrantedAuthority("ROLE_REGISTRATION_PROCESSOR"))));
		users.add(new User("id-auth", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_ID_AUTHENTICATION"))));
		users.add(new User("individual", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_INDIVIDUAL"))));
		users.add(new User("test", "mosip", Arrays.asList(new SimpleGrantedAuthority("ROLE_TEST"))));
		return new InMemoryUserDetailsManager(users);
	}

	@Bean
	public AfterburnerModule afterburnerModule() {
		return new AfterburnerModule();
	}

>>>>>>> 5ba3193 (MOSIP-32456)
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedMethods("*");
			}
		};
	}

<<<<<<< HEAD
	/**
	 * Configures the password encoder for encoding user passwords securely.
	 * 
	 * @return BCryptPasswordEncoder instance
	 */
=======
>>>>>>> 5ba3193 (MOSIP-32456)
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}