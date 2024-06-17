package io.mosip.kernel.bio.converter;

import io.mosip.kernel.bio.converter.config.TestSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

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
@Import(TestSecurityConfig.class)
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