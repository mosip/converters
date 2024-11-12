package io.mosip.kernel.bio.converter.dto;

import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.ToString;

/**
 * Data Transfer Object for conversion requests in the bio converter service.
 * 
 * <p>
 * This DTO encapsulates the data required for converting biometric data from
 * one format to another.
 * </p>
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * ConvertRequestDto request = new ConvertRequestDto();
 * request.setValues(valuesMap);
 * request.setSourceFormat("ISO19794_4_2011");
 * request.setTargetFormat("IMAGE/JPEG");
 * request.setSourceParameters(sourceParamsMap);
 * request.setTargetParameters(targetParamsMap);
 * </pre>
 * </p>
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */

@Data
@ToString
public class ConvertRequestDto {
	/**
	 * A map of values required for the conversion. The map must contain at least
	 * one entry.
	 */
	@NotNull(message = "Values code can not be null")
	@Size(min = 1, message = "Minimum one entry required")
	public Map<String, String> values;

	/**
	 * The format of the source biometric data. The format code cannot be null,
	 * blank, or empty.
	 */
	@NotNull(message = "SourceFormat code can not be null")
	@NotBlank(message = "SourceFormat code can not be blank")
	@NotEmpty(message = "SourceFormat code can not be empty")
	public String sourceFormat;

	/**
	 * The format of the target biometric data. The format code cannot be null,
	 * blank, or empty.
	 */
	@NotNull(message = "TargetFormat code can not be null")
	@NotBlank(message = "TargetFormat code can not be blank")
	@NotEmpty(message = "TargetFormat code can not be empty")
	public String targetFormat;

	/**
	 * A map of source parameters specific to the source format.
	 */
	@SuppressWarnings({ "java:S1104" })
	public Map<String, String> sourceParameters;

	/**
	 * A map of target parameters specific to the target format.
	 */
	@SuppressWarnings({ "java:S1104" })
	public Map<String, String> targetParameters;

	public ConvertRequestDto() {
		super();
	}

	public ConvertRequestDto(
			@NotNull(message = "Values code can not be null") @Size(min = 1, message = "Minimum one entry required") Map<String, String> values,
			@NotNull(message = "SourceFormat code can not be null") @NotBlank(message = "SourceFormat code can not be blank") @NotEmpty(message = "SourceFormat code can not be empty") String sourceFormat,
			@NotNull(message = "TargetFormat code can not be null") @NotBlank(message = "TargetFormat code can not be blank") @NotEmpty(message = "TargetFormat code can not be empty") String targetFormat,
			Map<String, String> sourceParameters, Map<String, String> targetParameters) {
		super();
		this.values = values;
		this.sourceFormat = sourceFormat;
		this.targetFormat = targetFormat;
		this.sourceParameters = sourceParameters;
		this.targetParameters = targetParameters;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ConvertRequestDto that = (ConvertRequestDto) o;
		return Objects.equals(values, that.values) && Objects.equals(sourceFormat, that.sourceFormat)
				&& Objects.equals(targetFormat, that.targetFormat)
				&& Objects.equals(sourceParameters, that.sourceParameters)
				&& Objects.equals(targetParameters, that.targetParameters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(values, sourceFormat, targetFormat, sourceParameters, targetParameters);
	}

	public boolean canEqual(Object other) {
		return other instanceof ConvertRequestDto;
	}
}