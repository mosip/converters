package io.mosip.kernel.bio.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.mosip.biometrics.util.face.ImageDataType;
import io.mosip.biometrics.util.finger.FingerImageCompressionType;
import io.mosip.biometrics.util.iris.ImageFormat;
import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;
import io.mosip.kernel.bio.converter.exception.ConversionException;
import io.mosip.kernel.bio.converter.service.impl.ConverterServiceImpl;

class ConverterServiceImplTest {

    private ConverterServiceImpl converterService;
    private String bioData = null;
    private String imageData = null;

	@BeforeEach
    void setUp() {
        converterService = new ConverterServiceImpl();
		FileInputStream fis;
		try {
			fis = new FileInputStream("src/test/resources/finger_wsq.txt");
			bioData = IOUtils.toString(fis, StandardCharsets.UTF_8);

			fis = new FileInputStream("src/test/resources/flower.jpeg");
			imageData = IOUtils.toString(fis, StandardCharsets.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    @Test
    void testConvert_withValidFingerISO_shouldReturnConvertedImage() {
        Map<String, String> values = new HashMap<>();
        values.put("Left IndexFinger", bioData);
        
        Map<String, String> sourceParams = new HashMap<>();
        Map<String, String> targetParams = new HashMap<>();
        
        Map<String, String> result = converterService.convert(values, "ISO19794_4_2011", "IMAGE/JPEG", sourceParams, targetParams);
        
        assertNotNull(result);
        assertTrue(result.containsKey("Left IndexFinger"));
        // further assertions based on expected output
    }

    @Test
    void testConvert_withUnsupportedSourceFormat_shouldThrowException() {
        Map<String, String> values = new HashMap<>();
        values.put("Left IndexFinger", bioData);
        
        assertThrows(ConversionException.class, () -> {
            converterService.convert(values, "ISO19794_2_2011", "IMAGE/JPEG", new HashMap<>(), new HashMap<>());
        });
    }

    @Test
    void testConvertFingerIsoToImageType_withInvalidBase64_shouldThrowException() {
        String invalidBase64 = "invalidBase64Data";
        
        assertThrows(ConversionException.class, () -> {
            converterService.convertFingerIsoToImageType(SourceFormatCode.ISO19794_4_2011, invalidBase64, TargetFormatCode.IMAGE_JPEG, new HashMap<>());
        });
    }

    @Test
    void testConvertBufferedImageToBytes_withInvalidTargetCode_shouldThrowException() {
        BufferedImage image = mock(BufferedImage.class);
        
        assertThrows(ConversionException.class, () -> {
            converterService.convertBufferedImageToBytes(TargetFormatCode.ISO19794_6_2011_PNG, image);
        });
    }
    
    @Test
    void decodeFingerImage_invalidFormat_throwsConversionException() {
        byte[] validImageData = imageData.getBytes(StandardCharsets.UTF_8);

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeFingerImage(validImageData, FingerImageCompressionType.NONE_BIT_PACKED),
            "Expected ConversionException for unsupported image format."
        );

        assertEquals(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void decodeFingerImage_ioExceptionDuringRead_throwsConversionException() throws Exception {
        byte[] faultyData = null;

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeFingerImage(faultyData, FingerImageCompressionType.JPEG_2000_LOSSY),
            "Expected ConversionException for IOException."
        );

        assertEquals(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void decodeFaceImage_invalidFormat_throwsConversionException() {
        byte[] validImageData = imageData.getBytes(StandardCharsets.UTF_8);

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeFaceImage(validImageData, ImageDataType.PNG),
            "Expected ConversionException for unsupported image format."
        );

        assertEquals(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void decodeFaceImage_ioExceptionDuringRead_throwsConversionException() throws Exception {
        byte[] faultyData = null;

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeFaceImage(faultyData, ImageDataType.JPEG2000_LOSSY),
            "Expected ConversionException for IOException."
        );

        assertEquals(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), exception.getErrorCode());
    }
    
    @Test
    void decodeIrisImage_invalidFormat_throwsConversionException() {
        byte[] validImageData = imageData.getBytes(StandardCharsets.UTF_8);

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeIrisImage(validImageData, ImageFormat.MONO_RAW),
            "Expected ConversionException for unsupported image format."
        );

        assertEquals(ConverterErrorCode.NOT_SUPPORTED_COMPRESSION_TYPE.getErrorCode(), exception.getErrorCode());
    }

    @Test
    void decodeIrisImage_ioExceptionDuringRead_throwsConversionException() throws Exception {
        byte[] faultyData = null;

        ConversionException exception = assertThrows(
            ConversionException.class,
            () -> converterService.decodeIrisImage(faultyData, ImageFormat.MONO_JPEG2000),
            "Expected ConversionException for IOException."
        );

        assertEquals(ConverterErrorCode.COULD_NOT_READ_ISO_IMAGE_DATA_EXCEPTION.getErrorCode(), exception.getErrorCode());
    }
}