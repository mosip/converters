package io.mosip.kernel.bio.converter.constant;

/**
 * Enum representing various parameter codes for the services. Each enum
 * constant includes a code and a descriptive message.
 * 
 * <p>
 * These parameters are used to specify different properties of an image, such
 * as DPI, width, and height.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * ParameterCode paramCode = ParameterCode.fromCode("dpi");
 * String code = paramCode.getCode();
 * String message = paramCode.getMessage();
 * </pre>
 * </p>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public enum ParameterCode {
	DPI("dpi", "image Dots Per Inch"), WIDTH("width", "image width"), HEIGHT("height", "image height");

	private final String code;
	private final String message;

	/**
	 * Constructor for initializing the enum constants with the specified code and
	 * message.
	 *
	 * @param code    the code representing the parameter
	 * @param message a descriptive message for the parameter
	 */
	private ParameterCode(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	/**
	 * Retrieves the code of the parameter.
	 *
	 * @return the code of the parameter
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Retrieves the parameter code enum constant for the specified code.
	 *
	 * @param codeName the code representing the parameter
	 * @return the corresponding {@code ParameterCode} enum constant, or
	 *         {@code null} if no matching constant is found
	 */
	public static ParameterCode fromCode(String codeName) {
		for (ParameterCode paramCode : ParameterCode.values()) {
			if (paramCode.getCode().equalsIgnoreCase(codeName)) {
				return paramCode;
			}
		}
		return null;
	}

	/**
	 * Retrieves the descriptive message of the parameter.
	 *
	 * @return the descriptive message of the parameter
	 */
	public String getMessage() {
		return message;
	}
}
