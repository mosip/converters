package io.mosip.kernel.bio.converter.service.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jnbis.api.model.Bitmap;
import org.jnbis.internal.WsqDecoder;
import org.springframework.stereotype.Service;

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
 * This class implements handling conversion of ISO format to JPEG or PNG Image
 * format
 * 
 * @author Janardhan B S
 * @since 1.0.0
 * 
 */
@Service
public class ConverterServiceImpl implements IConverterApi {
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

	public BufferedImage decodeFingerImage(byte[] imageData, int compressionType) throws ConversionException {
	    try {
	        switch (compressionType) {
	            case FingerImageCompressionType.JPEG_2000_LOSSY:
	            case FingerImageCompressionType.JPEG_2000_LOSS_LESS:
	                return ImageIO.read(new ByteArrayInputStream(imageData));
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

	public BufferedImage decodeFaceImage(byte[] imageData, int imageDataType) throws ConversionException {
	    try {
	        if (imageDataType == ImageDataType.JPEG2000_LOSSY || imageDataType == ImageDataType.JPEG2000_LOSS_LESS) {
	            return ImageIO.read(new ByteArrayInputStream(imageData));
	        } else {
	            throw new ConversionException(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(),
	            		ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
	        }
	    } catch (IOException | NullPointerException e) {
	        throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), 
	                                      e.getLocalizedMessage());
	    }
	}
	
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

	public BufferedImage decodeIrisImage(byte[] imageData, int imageFormat) throws ConversionException {
	    try {
	        if (imageFormat == ImageFormat.MONO_JPEG2000) {
	            return ImageIO.read(new ByteArrayInputStream(imageData));
	        } else {
	            throw new ConversionException(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(),
	            		ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorMessage());
	        }
	    } catch (IOException | NullPointerException e) {
	        throw new ConversionException(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(),
	                                      e.getLocalizedMessage());
	    }
	}
	
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