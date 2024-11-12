package io.mosip.kernel.bio.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

import io.mosip.kernel.bio.converter.config.TestSecurityConfig;

/**
 * Main Spring Boot application class for testing purposes.
 * 
 * <p>
 * This class initializes a Spring Boot application with specific
 * configurations:
 * </p>
 * <ul>
 * <li>It scans packages defined by "${mosip.auth.adapter.impl.basepackage}" and
 * "io.mosip.kernel.bio.*".</li>
 * <li>Excludes DataSourceAutoConfiguration to avoid automatic datasource
 * configuration.</li>
 * <li>Enables asynchronous execution of methods annotated with @Async.</li>
 * <li>Imports additional security configuration from
 * {@link TestSecurityConfig}.</li>
 * </ul>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = {
		"${mosip.auth.adapter.impl.basepackage}, io.mosip.kernel.bio.*" }, exclude = {
				DataSourceAutoConfiguration.class })
@EnableAsync
@Import(io.mosip.kernel.bio.converter.config.TestSecurityConfig.class)
@ComponentScan(
	    basePackages = "io.mosip.kernel",
	    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {io.mosip.kernel.auth.defaultadapter.config.SecurityConfig.class})
	)
public class TestBootApplication {

	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * @param args command line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(TestBootApplication.class, args);
	}
}