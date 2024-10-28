package io.mosip.kernel.bio.converter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import io.mosip.kernel.bio.converter.config.LoggerConfig;
import io.mosip.kernel.core.logger.spi.Logger;

class ConfigTest {

	@Test
	void testLogConfigReturnsLogger() {
		// Verify that logConfig method returns a non-null logger instance
		Logger logger = LoggerConfig.logConfig(LoggerConfig.class);
		assertNotNull(logger, "Logger should not be null");
	}
}