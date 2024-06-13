package io.mosip.kernel.bio.converter.exception;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.core.exception.ServiceError;
import io.mosip.kernel.core.http.ResponseWrapper;
import io.mosip.kernel.core.util.EmptyCheckUtils;

/**
 * ConversionHandlerControllerAdvice for capturing the errors build the response
 * wrapper around services errors and give ResponseEntity .
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
@RestControllerAdvice
public class ConversionExceptionAdvice {
	private static final Logger logger = LoggerFactory.getLogger(ConversionExceptionAdvice.class);

	private ObjectMapper objectMapper;

	@Autowired
	public ConversionExceptionAdvice(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@ExceptionHandler(value = { Exception.class, RuntimeException.class, ConversionException.class })
	public ResponseEntity<ResponseWrapper<ServiceError>> defaultServiceErrorHandler(HttpServletRequest request,
			Exception e) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = setErrors(request);
		ServiceError error = null;
		if (e instanceof ConversionException conversionException) {
			error = new ServiceError(conversionException.getErrorCode(), conversionException.getMessage());
		} else {
			error = new ServiceError(ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION.getErrorCode(), e.getMessage());
		}
		responseWrapper.getErrors().add(error);
		logger.error("Exception Root Cause: {} ", e.getMessage());
		logger.debug("Exception Root Cause:", e);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.APPLICATION_JSON)
				.body(responseWrapper);
	}

	private ResponseWrapper<ServiceError> setErrors(HttpServletRequest httpServletRequest) throws IOException {
		ResponseWrapper<ServiceError> responseWrapper = new ResponseWrapper<>();
		String requestBody = null;
		if (httpServletRequest instanceof ContentCachingRequestWrapper requestWrapper) {
			requestBody = new String(requestWrapper.getContentAsByteArray());
		}
		if (EmptyCheckUtils.isNullEmpty(requestBody)) {
			return responseWrapper;
		}
		JsonNode reqNode = objectMapper.readTree(requestBody);
		responseWrapper.setId(reqNode.path("id").asText());
		responseWrapper.setVersion(reqNode.path("version").asText());
		return responseWrapper;
	}
}