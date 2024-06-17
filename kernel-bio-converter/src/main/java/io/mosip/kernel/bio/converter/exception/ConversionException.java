package io.mosip.kernel.bio.converter.exception;

import io.mosip.kernel.core.exception.BaseUncheckedException;

/**
 * Custom Exception Class for handling errors that occur in the bio converter
 * services.
 * 
 * <p>
 * This exception is thrown when there is an error during the conversion
 * process. It extends
 * {@link io.mosip.kernel.core.exception.BaseUncheckedException}.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * if (invalidCondition) {
 * 	throw new ConversionException("MOS-CNV-001", "Invalid conversion parameter");
 * }
 * </pre>
 * </p>
 * 
 * @see io.mosip.kernel.core.exception.BaseUncheckedException
 * @author Janardhan B S
 * @since 1.0.0
 */

public class ConversionException extends BaseUncheckedException {
	/**
	 * Generated serial version id
	 */
	private static final long serialVersionUID = 687991492884005033L;

	/**
	 * Constructs a new ConversionException with the specified error code and error
	 * message.
	 * 
	 * @param errorCode    The error code for this exception.
	 * @param errorMessage The error message for this exception.
	 */
	public ConversionException(String errorCode, String errorMessage) {
		super(errorCode, errorMessage);
	}

	/**
	 * Constructs a new ConversionException with the specified error code, error
	 * message, and root cause.
	 * 
	 * @param errorCode    The error code for this exception.
	 * @param errorMessage The error message for this exception.
	 * @param rootCause    The cause of this exception.
	 */
	public ConversionException(String errorCode, String errorMessage, Throwable rootCause) {
		super(errorCode, errorMessage, rootCause);
	}
}