package io.mosip.kernel.bio.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * The entry point for the Kernel Bio Converter Application.
 * 
 * <p>
 * This class configures and launches the Spring Boot application. It excludes
 * the {@link DataSourceAutoConfiguration} class from auto-configuration since
 * the application does not require a datasource configuration. The
 * {@code scanBasePackages} attribute in {@link SpringBootApplication}
 * annotation is used to define the base packages to scan for Spring components.
 * </p>
 * 
 * @see SpringApplication
 * @see SpringBootApplication
 * @see EnableAutoConfiguration
 * @see DataSourceAutoConfiguration
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
@SpringBootApplication(scanBasePackages = { "${mosip.auth.adapter.impl.basepackage}, io.mosip.kernel.bio.*" })
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
@ComponentScan(
	    basePackages = "io.mosip.kernel",
	    excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {io.mosip.kernel.auth.defaultadapter.config.SecurityConfig.class})
	)
public class KernelBioConverterApplication {
	/**
	 * The main method which serves as the entry point for the Spring Boot
	 * application.
	 * 
	 * @param args command-line arguments (not used).
	 */
	public static void main(String[] args) {
		SpringApplication.run(KernelBioConverterApplication.class, args);
	}
}