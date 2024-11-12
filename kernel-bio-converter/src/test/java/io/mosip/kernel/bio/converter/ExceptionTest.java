package io.mosip.kernel.bio.converter;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.bio.converter.exception.ConversionException;
import io.mosip.kernel.bio.converter.exception.ConversionExceptionAdvice;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import jakarta.servlet.http.HttpServletRequest;

class ExceptionTest {
	@Mock
	private ObjectMapper objectMapper;

	@InjectMocks
	private ConversionExceptionAdvice advice;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

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

	@Test
	void testConversionExceptionAdviceSetErrors_withValidJsonContent() throws Exception {
		// Mock the HttpServletRequest and ObjectMapper
		HttpServletRequest request = mock(ContentCachingRequestWrapper.class);
		when(((ContentCachingRequestWrapper) request).getContentAsByteArray())
				.thenReturn("{\"id\":\"123\", \"version\":\"1.0\"}".getBytes());

		JsonNode mockNode = new ObjectMapper().readTree("{\"id\":\"123\", \"version\":\"1.0\"}");
		when(objectMapper.readTree(any(String.class))).thenReturn(mockNode);

		ResponseWrapper<ServiceError> result = advice.setErrors(request);

		assertEquals("123", result.getId());
		assertEquals("1.0", result.getVersion());
	}

	@Test
	void testConversionExceptionAdviceSetErrors_withEmptyRequestBody() throws Exception {
		HttpServletRequest request = mock(ContentCachingRequestWrapper.class);
		when(((ContentCachingRequestWrapper) request).getContentAsByteArray()).thenReturn(new byte[0]); // Empty content

		ResponseWrapper<ServiceError> result = advice.setErrors(request);

		assertNull(result.getId());
		assertNull(result.getVersion());
	}

	@Test
	void testConversionExceptionAdviceSetErrors_withInvalidJsonContent() throws Exception {
		HttpServletRequest request = mock(ContentCachingRequestWrapper.class);
		when(((ContentCachingRequestWrapper) request).getContentAsByteArray()).thenReturn("{invalidJson}".getBytes());

		 // Mocking ObjectMapper to throw JsonProcessingException on invalid JSON
	    when(objectMapper.readTree(any(String.class))).thenThrow(new JsonProcessingException("Invalid JSON") {});

	    // Verifying if IOException is thrown when invalid JSON is processed
	    assertThrows(Exception.class, () -> advice.setErrors(request));
	}
}