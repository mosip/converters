package io.mosip.kernel.bio.converter.constant;

import io.mosip.kernel.bio.converter.exception.ConversionException;

/**
 * Enum representing various source format codes for the services. Each enum
 * constant includes a code and a descriptive message.
 * 
 * <p>
 * These formats are used to specify the source format of biometric data
 * conversion.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * SourceFormatCode formatCode = SourceFormatCode.fromCode("ISO19794_4_2011");
 * String code = formatCode.getCode();
 * String message = formatCode.getMessage();
 * </pre>
 * </p>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */

public enum SourceFormatCode {
	ISO19794_4_2011("ISO19794_4_2011", "Finger ISO format"), ISO19794_5_2011("ISO19794_5_2011", "Face ISO format"),
	ISO19794_6_2011("ISO19794_6_2011", "Iris ISO format");

	private final String code;
	private final String message;

	/**
	 * Constructor for initializing the enum constants with the specified code and
	 * message.
	 *
	 * @param code    the code representing the source format
	 * @param message a descriptive message for the source format
	 */
	private SourceFormatCode(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Retrieves the code of the source format.
	 *
	 * @return the code of the source format
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Retrieves the source format code enum constant for the specified code.
	 *
	 * @param sourceCodeName the code representing the source format
	 * @return the corresponding {@code SourceFormatCode} enum constant
	 * @throws ConversionException if the specified code does not match any enum
	 *                             constant
	 */
	public static SourceFormatCode fromCode(String sourceCodeName) {
		for (SourceFormatCode sourceCode : SourceFormatCode.values()) {
			if (sourceCode.getCode().equalsIgnoreCase(sourceCodeName)) {
				return sourceCode;
			}
		}
		throw new ConversionException(ConverterErrorCode.INVALID_SOURCE_EXCEPTION.getErrorCode(),
				ConverterErrorCode.INVALID_SOURCE_EXCEPTION.getErrorMessage());
	}

	/**
	 * Checks if the specified code is a valid source format code.
	 *
	 * @param sourceCodeName the code to be checked
	 * @return {@code true} if the code is valid; {@code false} otherwise
	 */
	public static boolean validCode(String sourceCodeName) {
		for (SourceFormatCode sourceCode : SourceFormatCode.values()) {
			if (sourceCode.getCode().equalsIgnoreCase(sourceCodeName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves the descriptive message of the source format.
	 *
	 * @return the descriptive message of the source format
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the string representation of the source format code.
	 *
	 * @return the code of the source format
	 */
	@Override
	public String toString() {
		return getCode();
	}
}
