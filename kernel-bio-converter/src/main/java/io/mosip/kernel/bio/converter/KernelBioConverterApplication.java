package io.mosip.kernel.bio.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication (scanBasePackages = {"${mosip.auth.adapter.impl.basepackage}, io.mosip.kernel.bio.*"})
@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class })
public class KernelBioConverterApplication {
	public static void main(String[] args) {
		SpringApplication.run(KernelBioConverterApplication.class, args);
	}
}