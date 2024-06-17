package io.mosip.kernel.bio.converter.config;

import io.mosip.kernel.core.logger.spi.Logger;
import io.mosip.kernel.logger.logback.factory.Logfactory;

/**
 * Configuration class for setting up logger instances for the application. This
 * class provides a method to retrieve a logger instance for a given class.
 */
public final class LoggerConfig {
	/**
	 * Private constructor to prevent instantiation. This is a LoggerConfig class,
	 * and should not be instantiated.
	 */
	private LoggerConfig() {
	}

	/**
	 * Returns a logger instance for the specified class.
	 *
	 * @param clazz the class for which the logger instance is to be created
	 * @return the logger instance for the specified class
	 */
	public static Logger logConfig(Class<?> clazz) {
		return Logfactory.getSlf4jLogger(clazz);
	}
}