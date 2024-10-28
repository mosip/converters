package io.mosip.kernel.bio.converter;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.mosip.kernel.bio.converter.exception.ConversionException;

class ExceptionTest {
	@Test
	void testConversionExceptionConstructorWithErrorCodeAndMessage() {
		String errorCode = "MOS-CNV-001";
		String errorMessage = "Input Source Request may be null or Source Format may be null or Target Format may be null";

		ConversionException exception = new ConversionException(errorCode, errorMessage);

		assertEquals(errorCode, exception.getErrorCode());
		assertTrue(exception.getMessage().contains(errorMessage));
		assertNull(exception.getCause()); // Ensure there is no root cause
	}

	@Test
	void testConversionExceptionConstructorWithErrorCodeMessageAndRootCause() {
		String errorCode = "MOS-CNV-001";
		String errorMessage = "Input Source Request may be null or Source Format may be null or Target Format may be null";
		Throwable rootCause = new NullPointerException("Null pointer exception");

		ConversionException exception = new ConversionException(errorCode, errorMessage, rootCause);

		assertEquals(errorCode, exception.getErrorCode());
		assertTrue(exception.getMessage().contains(errorMessage));
		assertEquals(rootCause, exception.getCause()); // Ensure the root cause is set
	}

	@Test
	void testConversionExceptionExceptionMessage() {
		String errorCode = "MOS-CNV-001";
		String errorMessage = "Input Source Request may be null or Source Format may be null or Target Format may be null";

		ConversionException exception = new ConversionException(errorCode, errorMessage);

		String expectedMessage = "MOS-CNV-001: Input Source Request may be null or Source Format may be null or Target Format may be null";
		assertTrue(exception.getMessage().contains(errorMessage));
	}
}