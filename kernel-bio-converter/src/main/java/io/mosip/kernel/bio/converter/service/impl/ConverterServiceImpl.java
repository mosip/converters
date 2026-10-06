package io.mosip.kernel.bio.converter.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.springframework.stereotype.Service;

import com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.biometrics.util.ConvertRequestDto;
import io.mosip.biometrics.util.face.FaceBDIR;
import io.mosip.biometrics.util.face.FaceDecoder;
import io.mosip.biometrics.util.face.ImageDataType;
import io.mosip.biometrics.util.finger.FingerBDIR;
import io.mosip.biometrics.util.finger.FingerDecoder;
import io.mosip.biometrics.util.finger.FingerImageCompressionType;
import io.mosip.biometrics.util.iris.ImageFormat;
import io.mosip.biometrics.util.iris.IrisBDIR;
import io.mosip.biometrics.util.iris.IrisDecoder;
import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;
import io.mosip.kernel.bio.converter.exception.ConversionException;
import io.mosip.kernel.bio.converter.service.IConverterApi;

/**
 * Service implementation that converts ISO/IEC 19794 biometric records
 * (finger, face, iris) into JPEG/PNG image bytes (URL-safe Base64), or
 * re-wraps into supported ISO target formats via {@code biometrics-util}.
 *
 * <p>
 * Entry point: {@link #convert(Map, String, String, Map, Map)}. Per-modality
 * helpers decode Base64, parse the BDIR, decompress JP2000/WSQ, and re-encode.
 * </p>
 *
 * @author Janardhan B S
 * @since 1.0.0
 */
@Service
public class ConverterServiceImpl implements IConverterApi {
	/**
	 * Converts each Base64 URL-encoded ISO blob in {@code values} from
	 * {@code sourceFormat} to {@code targetFormat}.
	 *
	 * @param values            identifier → Base64 URL-encoded ISO BDB
	 * @param sourceFormat      e.g. {@code ISO19794_4_2011}
	 * @param targetFormat      e.g. {@code IMAGE/JPEG}
	 * @param sourceParameters  optional source hints (unused / ignored if unknown)
	 * @param targetParameters  optional target hints (unused / ignored if unknown)
	 * @return map of the same keys → Base64 URL-encoded converted payloads
	 * @throws ConversionException when input is empty, formats are invalid, or decode fails
	 */
	@Override
	public Map<String, String> convert(Map<String, String> values, String sourceFormat, String targetFormat,
			Map<String, String> sourceParameters, Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;
		if (values == null || values.size() == 0)
			throw new ConversionException(errorCode.getErrorCode(), errorCode.getErrorMessage());

		Map<String, String> targetValues = new HashMap<>();

		SourceFormatCode sourceCode = SourceFormatCode.fromCode(sourceFormat);
		TargetFormatCode targetCode = TargetFormatCode.fromCode(targetFormat);
		for (Map.Entry<String, String> entry : values.entrySet()) {
			String targetValue = null;
			String isoData = entry.getValue();
			if (isoData == null || isoData.trim().length() == 0) {
				errorCode = ConverterErrorCode.SOURCE_CAN_NOT_BE_EMPTY_OR_NULL_EXCEPTION;
				throw new ConversionException(errorCode.getErrorCode(), errorCode.getErrorMessage());
			}

			switch (sourceCode) {
			// FINGER ISO can have JP2000 or WSQ
			case ISO19794_4_2011:
				targetValue = convertFingerIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
				break;
			// FACE ISO can have JP2000
			case ISO19794_5_2011:
				targetValue = convertFaceIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
				break;
			// IRIS ISO can have JP2000
			case ISO19794_6_2011:
				targetValue = convertIrisIsoToImageType(sourceCode, entry.getValue(), targetCode, targetParameters);
				break;
			default:
				errorCode = ConverterErrorCode.INVALID_SOURCE_EXCEPTION;
				throw new ConversionException(errorCode.getErrorCode(), errorCode.getErrorMessage());
			}
			targetValues.put(entry.getKey(), targetValue);
		}
		return targetValues;
	}

	/**
	 * Decodes a finger ISO19794-4 payload and re-encodes the embedded image to
	 * {@code targetCode} (JPEG or PNG).
	 *
	 * @param sourceCode       resolved source format enum
	 * @param isoData          Base64 URL-encoded finger ISO blob
	 * @param targetCode       resolved target image format
	 * @param targetParameters optional target parameters (currently unused)
	 * @return Base64 URL-encoded image bytes
	 * @throws ConversionException on Base64, ISO structure, or compression failures
	 */
	@SuppressWarnings({ "java:S1172", "java:S6208" })
	public String convertFingerIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode,
			Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Finger");
		requestDto.setVersion(sourceCode.getCode());

		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64(isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		FingerBDIR bdir;
		int inCompressionType = -1;
		byte[] inImageData = null;
		try {
			bdir = FingerDecoder.getFingerBDIR(requestDto);

			inCompressionType = bdir.getCompressionType();
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_FINGER_ISO_FORMAT_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = decodeFingerImage(inImageData, inCompressionType);
		byte[] outImageData = convertBufferedImageToBytes(targetCode, outImage);
	    return CommonUtil.encodeToURLSafeBase64(outImageData);
	}

	/**
	 * Decompresses finger image bytes for the given ISO compression type.
	 *
	 * @param imageData        raw image payload from the finger BDIR
	 * @param compressionType  {@link FingerImageCompressionType} constant
	 * @return decoded {@link BufferedImage}
	 * @throws ConversionException if compression is unsupported or bytes cannot be read
	 */
	public BufferedImage decodeFingerImage(byte[] imageData, int compressionType) throws ConversionException {
	    try {
	        switch (compressionType) {
	            case FingerImageCompressionType.JPEG_2000_LOSSY:
	            case FingerImageCompressionType.JPEG_2000_LOSS_LESS:
	                return readJpeg2000(imageData);
	            case FingerImageCompressionType.WSQ:
	                WsqDecoder decoder = new WsqDecoder();
	                Bitmap bitmap = decoder.decode(imageData);
	                return CommonUtil.convert(bitmap);
	            default:
	                throw new ConversionException(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(),
	                		ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
	        }
	    } catch (IOException | NullPointerException e) {
	        throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), 
	                                      e.getLocalizedMessage());
	    }
	}

	/**
	 * Decodes a face ISO19794-5 payload and re-encodes to {@code targetCode}.
	 *
	 * @param sourceCode       resolved source format enum
	 * @param isoData          Base64 URL-encoded face ISO blob
	 * @param targetCode       resolved target image format
	 * @param targetParameters optional target parameters (currently unused)
	 * @return Base64 URL-encoded image bytes
	 * @throws ConversionException on Base64, ISO structure, or compression failures
	 */
	@SuppressWarnings({ "java:S1172" })
	public String convertFaceIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode,
			Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Face");
		requestDto.setVersion(sourceCode.getCode());
		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64(isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		FaceBDIR bdir;
		int inImageDataType = -1;
		byte[] inImageData = null;
		try {
			bdir = FaceDecoder.getFaceBDIR(requestDto);

			inImageDataType = bdir.getImageDataType();
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_FACE_ISO_FORMAT_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = decodeFaceImage(inImageData, inImageDataType);
	    byte[] outImageData = convertBufferedImageToBytes(targetCode, outImage);
	    return CommonUtil.encodeToURLSafeBase64(outImageData);
	}

	/**
	 * Decompresses face image bytes (JPEG2000 lossy/lossless only).
	 *
	 * @param imageData     raw image payload from the face BDIR
	 * @param imageDataType {@link ImageDataType} constant
	 * @return decoded {@link BufferedImage}
	 * @throws ConversionException if type is unsupported or bytes cannot be read
	 */
	public BufferedImage decodeFaceImage(byte[] imageData, int imageDataType) throws ConversionException {
	    try {
	        if (imageDataType == ImageDataType.JPEG2000_LOSSY || imageDataType == ImageDataType.JPEG2000_LOSS_LESS) {
	            return readJpeg2000(imageData);
	        } else {
	            throw new ConversionException(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(),
	            		ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
	        }
	    } catch (IOException | NullPointerException e) {
	        throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), 
	                                      e.getLocalizedMessage());
	    }
	}
	
	/**
	 * Decodes an iris ISO19794-6 payload and re-encodes to {@code targetCode}.
	 *
	 * @param sourceCode       resolved source format enum
	 * @param isoData          Base64 URL-encoded iris ISO blob
	 * @param targetCode       resolved target image format
	 * @param targetParameters optional target parameters (currently unused)
	 * @return Base64 URL-encoded image bytes
	 * @throws ConversionException on Base64, ISO structure, or compression failures
	 */
	@SuppressWarnings({ "java:S1172" })
	public String convertIrisIsoToImageType(SourceFormatCode sourceCode, String isoData, TargetFormatCode targetCode,
			Map<String, String> targetParameters) throws ConversionException {
		ConverterErrorCode errorCode = ConverterErrorCode.TECHNICAL_ERROR_EXCEPTION;

		ConvertRequestDto requestDto = new ConvertRequestDto();
		requestDto.setModality("Iris");
		requestDto.setVersion(sourceCode.getCode());
		try {
			requestDto.setInputBytes(CommonUtil.decodeURLSafeBase64(isoData));
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_BASE64URLENCODED_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		int inImageFormat = -1;
		byte[] inImageData = null;
		IrisBDIR bdir;
		try {
			bdir = IrisDecoder.getIrisBDIR(requestDto);

			inImageFormat = bdir.getImageFormat();
			inImageData = bdir.getImage();
		} catch (Exception e) {
			errorCode = ConverterErrorCode.SOURCE_NOT_VALID_IRIS_ISO_FORMAT_EXCEPTION;
			throw new ConversionException(errorCode.getErrorCode(), e.getLocalizedMessage());
		}

		BufferedImage outImage = decodeIrisImage(inImageData, inImageFormat);
	    byte[] outImageData = convertBufferedImageToBytes(targetCode, outImage);
	    return CommonUtil.encodeToURLSafeBase64(outImageData);
	}

	/**
	 * Decompresses iris image bytes ({@link ImageFormat#MONO_JPEG2000} only).
	 *
	 * @param imageData   raw image payload from the iris BDIR
	 * @param imageFormat {@link ImageFormat} constant
	 * @return decoded {@link BufferedImage}
	 * @throws ConversionException if format is unsupported or bytes cannot be read
	 */
	public BufferedImage decodeIrisImage(byte[] imageData, int imageFormat) throws ConversionException {
	    try {
	        if (imageFormat == ImageFormat.MONO_JPEG2000) {
	            return readJpeg2000(imageData);
	        } else {
	            throw new ConversionException(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(),
	            		ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
	        }
	    } catch (IOException | NullPointerException e) {
	        throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(),
	                                      e.getLocalizedMessage());
	    }
	}
	
	/**
	 * Reads JPEG2000 bytes into a {@link BufferedImage}.
	 *
	 * <p>
	 * {@link ImageIO#read} returns {@code null} when no reader is registered,
	 * which happens when the ImageIO registry was initialised before a nested
	 * (Spring Boot fat JAR) class loader exposed the jai-imageio SPI. In that
	 * case the registry is rescanned and, failing that, the JPEG2000 reader
	 * is used directly.
	 * </p>
	 *
	 * @param imageData JPEG2000 encoded bytes
	 * @return decoded image, never {@code null}
	 * @throws IOException         if the bytes cannot be read
	 * @throws ConversionException if the bytes are not decodable JPEG2000
	 */
	public BufferedImage readJpeg2000(byte[] imageData) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
		if (image == null) {
			ImageIO.scanForPlugins();
			image = ImageIO.read(new ByteArrayInputStream(imageData));
		}
		if (image == null)
			image = readWithJ2KReader(imageData);
		if (image == null)
			throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(),
					ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorMessage()
							+ ": no JPEG2000 reader could decode the image");
		return image;
	}

	private static BufferedImage readWithJ2KReader(byte[] imageData) throws IOException {
		J2KImageReaderSpi spi = new J2KImageReaderSpi();
		try (ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(imageData))) {
			if (input == null || !spi.canDecodeInput(input))
				return null;
			ImageReader reader = spi.createReaderInstance();
			try {
				reader.setInput(input, true, true);
				return reader.read(0);
			} finally {
				reader.dispose();
			}
		}
	}

	/**
	 * Encodes a {@link BufferedImage} to JPEG or PNG bytes for the given target.
	 *
	 * @param targetCode image target enum ({@code IMAGE_JPEG} or {@code IMAGE_PNG})
	 * @param outImage   decoded biometric image
	 * @return encoded image bytes
	 * @throws ConversionException if {@code targetCode} is not a plain image format
	 */
	public byte[] convertBufferedImageToBytes(TargetFormatCode targetCode, BufferedImage outImage) {
		switch (targetCode) {
		case IMAGE_JPEG:
			return CommonUtil.convertBufferedImageToJPEGBytes(outImage);
		case IMAGE_PNG:
			return CommonUtil.convertBufferedImageToPNGBytes(outImage);
		default:
			throw new ConversionException(ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorCode(),
					ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorMessage());
		}
	}
}