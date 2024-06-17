package io.mosip.kernel.bio.converter.constant;

import io.mosip.kernel.bio.converter.exception.ConversionException;

/**
 * Enum representing various target format codes for the services. Each enum
 * constant includes a code and a descriptive message.
 * 
 * <p>
 * These formats are used to specify the target format of biometric data
 * conversion.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * TargetFormatCode formatCode = TargetFormatCode.fromCode("IMAGE/JPEG");
 * String code = formatCode.getCode();
 * String message = formatCode.getMessage();
 * </pre>
 * </p>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public enum TargetFormatCode {
	IMAGE_JPEG("IMAGE/JPEG", "jpeg format"), IMAGE_PNG("IMAGE/PNG", "png format"),
	ISO19794_4_2011_JPEG("ISO19794_4_2011/JPEG", "Finger ISO format to Finger ISO format with JPEG IMAGE"),
	ISO19794_5_2011_JPEG("ISO19794_5_2011/JPEG", "Face ISO format to Finger ISO format with JPEG IMAGE"),
	// ISO19794_6_2011_JPEG("ISO19794_6_2011/JPEG", "Iris ISO format to Finger ISO
	// format with JPEG IMAGE"),
	ISO19794_4_2011_PNG("ISO19794_4_2011/PNG", "Finger ISO format to Finger ISO format with PNG IMAGE"),
	ISO19794_5_2011_PNG("ISO19794_5_2011/PNG", "Face ISO format to Finger ISO format with PNG IMAGE"),
	ISO19794_6_2011_PNG("ISO19794_6_2011/PNG", "Iris ISO format to Finger ISO format with PNG IMAGE");

	private final String code;
	private final String message;

	/**
	 * Constructor for initializing the enum constants with the specified code and
	 * message.
	 *
	 * @param code    the code representing the target format
	 * @param message a descriptive message for the target format
	 */
	private TargetFormatCode(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Retrieves the code of the target format.
	 *
	 * @return the code of the target format
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Retrieves the target format code enum constant for the specified code.
	 *
	 * @param targetCodeName the code representing the target format
	 * @return the corresponding {@code TargetFormatCode} enum constant
	 * @throws ConversionException if the specified code does not match any enum
	 *                             constant
	 */
	public static TargetFormatCode fromCode(String targetCodeName) {
		for (TargetFormatCode targetCode : TargetFormatCode.values()) {
			if (targetCode.getCode().equalsIgnoreCase(targetCodeName)) {
				return targetCode;
			}
		}
		throw new ConversionException(ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorCode(),
				ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorMessage());
	}

	/**
	 * Checks if the specified code is a valid target format code.
	 *
	 * @param targetCodeName the code to be checked
	 * @return {@code true} if the code is valid; {@code false} otherwise
	 */
	public static boolean validCode(String targetCodeName) {
		for (TargetFormatCode targetCode : TargetFormatCode.values()) {
			if (targetCode.getCode().equalsIgnoreCase(targetCodeName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves the descriptive message of the target format.
	 *
	 * @return the descriptive message of the target format
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the string representation of the target format code.
	 *
	 * @return the code of the target format
	 */
	@Override
	public String toString() {
		return getCode();
	}
}
