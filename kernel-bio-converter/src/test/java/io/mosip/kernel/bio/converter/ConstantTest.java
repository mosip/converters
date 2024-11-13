package io.mosip.kernel.bio.converter;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.mosip.kernel.bio.converter.constant.ConverterErrorCode;
import io.mosip.kernel.bio.converter.constant.ParameterCode;
import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;
import io.mosip.kernel.bio.converter.exception.ConversionException;

class ConstantTest {
	@Test
	void testParameterCodeGetCode() {
		assertEquals("dpi", ParameterCode.DPI.getCode());
		assertEquals("width", ParameterCode.WIDTH.getCode());
		assertEquals("height", ParameterCode.HEIGHT.getCode());
	}

	@Test
	void testParameterCodeGetMessage() {
		assertEquals("image Dots Per Inch", ParameterCode.DPI.getMessage());
		assertEquals("image width", ParameterCode.WIDTH.getMessage());
		assertEquals("image height", ParameterCode.HEIGHT.getMessage());
	}

	@Test
	void testParameterCodeFromCode_ValidCodes() {
		assertEquals(ParameterCode.DPI, ParameterCode.fromCode("dpi"));
		assertEquals(ParameterCode.WIDTH, ParameterCode.fromCode("width"));
		assertEquals(ParameterCode.HEIGHT, ParameterCode.fromCode("height"));
	}

	@Test
	void testParameterCodeFromCode_IgnoreCase() {
		assertEquals(ParameterCode.DPI, ParameterCode.fromCode("DPI"));
		assertEquals(ParameterCode.WIDTH, ParameterCode.fromCode("Width"));
		assertEquals(ParameterCode.HEIGHT, ParameterCode.fromCode("HEIGHT"));
	}

	@Test
	void testParameterCodeFromCode_InvalidCode() {
		assertNull(ParameterCode.fromCode("invalid"));
		assertNull(ParameterCode.fromCode("")); // Test empty string
		assertNull(ParameterCode.fromCode(null)); // Test null
	}

	@Test
	void testSourceFormatCodeGetCode() {
		assertEquals("ISO19794_4_2011", SourceFormatCode.ISO19794_4_2011.getCode());
		assertEquals("ISO19794_5_2011", SourceFormatCode.ISO19794_5_2011.getCode());
		assertEquals("ISO19794_6_2011", SourceFormatCode.ISO19794_6_2011.getCode());
	}

	@Test
	void testSourceFormatCodeGetMessage() {
		assertEquals("Finger ISO format", SourceFormatCode.ISO19794_4_2011.getMessage());
		assertEquals("Face ISO format", SourceFormatCode.ISO19794_5_2011.getMessage());
		assertEquals("Iris ISO format", SourceFormatCode.ISO19794_6_2011.getMessage());
	}

	@Test
	void testSourceFormatCodeFromCode_ValidCodes() {
		assertEquals(SourceFormatCode.ISO19794_4_2011, SourceFormatCode.fromCode("ISO19794_4_2011"));
		assertEquals(SourceFormatCode.ISO19794_5_2011, SourceFormatCode.fromCode("ISO19794_5_2011"));
		assertEquals(SourceFormatCode.ISO19794_6_2011, SourceFormatCode.fromCode("ISO19794_6_2011"));
	}

	@Test
	void testSourceFormatCodeFromCode_IgnoreCase() {
		assertEquals(SourceFormatCode.ISO19794_4_2011, SourceFormatCode.fromCode("iso19794_4_2011"));
		assertEquals(SourceFormatCode.ISO19794_5_2011, SourceFormatCode.fromCode("ISO19794_5_2011"));
		assertEquals(SourceFormatCode.ISO19794_6_2011, SourceFormatCode.fromCode("IsO19794_6_2011"));
	}

	@Test
	void testSourceFormatCodeFromCode_InvalidCode() {
		Exception exception = assertThrows(ConversionException.class, () -> {
			SourceFormatCode.fromCode("invalid");
		});
		assertEquals(ConverterErrorCode.INVALID_SOURCE_EXCEPTION.getErrorCode(),
				((ConversionException) exception).getErrorCode());
		assertTrue(((ConversionException) exception).getMessage()
				.contains(ConverterErrorCode.INVALID_SOURCE_EXCEPTION.getErrorMessage()));
	}

	@Test
	void testSourceFormatCodeValidCode_ValidCodes() {
		assertTrue(SourceFormatCode.validCode("ISO19794_4_2011"));
		assertTrue(SourceFormatCode.validCode("ISO19794_5_2011"));
		assertTrue(SourceFormatCode.validCode("ISO19794_6_2011"));
	}

	@Test
	void testSourceFormatCodeValidCode_InvalidCode() {
		assertFalse(SourceFormatCode.validCode("invalid"));
		assertFalse(SourceFormatCode.validCode(""));
		assertFalse(SourceFormatCode.validCode(null));
	}

	@Test
	void testSourceFormatCodeToString() {
		assertEquals("ISO19794_4_2011", SourceFormatCode.ISO19794_4_2011.toString());
		assertEquals("ISO19794_5_2011", SourceFormatCode.ISO19794_5_2011.toString());
		assertEquals("ISO19794_6_2011", SourceFormatCode.ISO19794_6_2011.toString());
	}
	
	@Test
    void testTargetFormatCodeGetCode() {
        assertEquals("IMAGE/JPEG", TargetFormatCode.IMAGE_JPEG.getCode());
        assertEquals("IMAGE/PNG", TargetFormatCode.IMAGE_PNG.getCode());
        assertEquals("ISO19794_4_2011/JPEG", TargetFormatCode.ISO19794_4_2011_JPEG.getCode());
        assertEquals("ISO19794_4_2011/PNG", TargetFormatCode.ISO19794_4_2011_PNG.getCode());
    }

    @Test
    void testTargetFormatCodeGetMessage() {
        assertEquals("jpeg format", TargetFormatCode.IMAGE_JPEG.getMessage());
        assertEquals("png format", TargetFormatCode.IMAGE_PNG.getMessage());
        assertEquals("Finger ISO format to Finger ISO format with JPEG IMAGE", 
                     TargetFormatCode.ISO19794_4_2011_JPEG.getMessage());
        assertEquals("Iris ISO format to Finger ISO format with PNG IMAGE", 
                     TargetFormatCode.ISO19794_6_2011_PNG.getMessage());
    }

    @Test
    void testTargetFormatCodeFromCode_ValidCodes() {
        assertEquals(TargetFormatCode.IMAGE_JPEG, TargetFormatCode.fromCode("IMAGE/JPEG"));
        assertEquals(TargetFormatCode.ISO19794_4_2011_JPEG, 
                     TargetFormatCode.fromCode("ISO19794_4_2011/JPEG"));
        assertEquals(TargetFormatCode.ISO19794_5_2011_PNG, 
                     TargetFormatCode.fromCode("ISO19794_5_2011/PNG"));
    }

    @Test
    void testTargetFormatCodeFromCode_IgnoreCase() {
        assertEquals(TargetFormatCode.IMAGE_JPEG, TargetFormatCode.fromCode("image/jpeg"));
        assertEquals(TargetFormatCode.ISO19794_4_2011_JPEG, 
                     TargetFormatCode.fromCode("iso19794_4_2011/jpeg"));
    }

    @Test
    void testTargetFormatCodeFromCode_InvalidCode() {
        Exception exception = assertThrows(ConversionException.class, () -> {
            TargetFormatCode.fromCode("invalid");
        });
        assertEquals(ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorCode(), 
                     ((ConversionException) exception).getErrorCode());
		assertTrue(((ConversionException) exception).getMessage()
				.contains(ConverterErrorCode.INVALID_TARGET_EXCEPTION.getErrorMessage()));
    }

    @Test
    void testTargetFormatCodeValidCode_ValidCodes() {
        assertTrue(TargetFormatCode.validCode("IMAGE/JPEG"));
        assertTrue(TargetFormatCode.validCode("ISO19794_4_2011/PNG"));
    }

    @Test
    void testTargetFormatCodeValidCode_InvalidCode() {
        assertFalse(TargetFormatCode.validCode("invalid"));
        assertFalse(TargetFormatCode.validCode(""));
        assertFalse(TargetFormatCode.validCode(null));
    }

    @Test
    void testTargetFormatCodeToString() {
        assertEquals("IMAGE/JPEG", TargetFormatCode.IMAGE_JPEG.toString());
        assertEquals("IMAGE/PNG", TargetFormatCode.IMAGE_PNG.toString());
        assertEquals("ISO19794_5_2011/JPEG", TargetFormatCode.ISO19794_5_2011_JPEG.toString());
    }
}