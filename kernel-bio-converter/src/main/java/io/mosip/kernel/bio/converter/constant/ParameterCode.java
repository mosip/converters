package io.mosip.kernel.bio.converter.constant;

/**
 * ParameterCode Enum for the services.
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public enum ParameterCode {
	DPI("dpi", "image Dots Per Inch"),
	WIDTH("width", "image width"),
	HEIGHT("height", "image height");

	private final String code;
	private final String message;

	private ParameterCode(final String code, final String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	 public static ParameterCode fromCode(String codeName) {
		 for (ParameterCode paramCode : ParameterCode.values()) {
	     	if (paramCode.getCode().equalsIgnoreCase(codeName)) {
	        	return paramCode;
	    	}
	    }
		return null;
	}
	 
	public String getMessage() {
		return message;
	}
}
