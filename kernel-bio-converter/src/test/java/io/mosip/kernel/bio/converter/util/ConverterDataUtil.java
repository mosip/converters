package io.mosip.kernel.bio.converter.util;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.biometrics.util.CommonUtil;
import io.mosip.kernel.bio.converter.constant.SourceFormatCode;
import io.mosip.kernel.bio.converter.constant.TargetFormatCode;

/**
 * Utility class for handling responses and validating image formats in
 * conversion operations.
 * 
 * @author Janardan B S
 * @since 1.0.0
 */
public class ConverterDataUtil {
	/**
	 * Checks the response from a REST API call for expected status and error codes.
	 *
	 * @param rst          The result of the MVC call to check.
	 * @param status       The expected HTTP status code.
	 * @param sourceCode   The source format code (unused in current
	 *                     implementation).
	 * @param expectedCode The expected error code in case of status 500.
	 */
	@SuppressWarnings({ "java:S1172", "unchecked" })
	public static void checkResponse(MvcResult rst, long status, SourceFormatCode sourceCode, String expectedCode) {
		try {
			ObjectMapper jsonMapper = new ObjectMapper();
			if (rst.getResponse().getContentAsString().isEmpty() && rst.getResponse().getStatus() == 404) {
				assertEquals(404, rst.getResponse().getStatus());
			} else {
				Map m = jsonMapper.readValue(rst.getResponse().getContentAsString(Charset.defaultCharset()), Map.class);
				assertEquals(status, rst.getResponse().getStatus());
				if (status == 500 && m.containsKey("errors") && null != m.get("errors")) {
					assertEquals(expectedCode, ((List<Map<String, String>>) m.get("errors")).get(0).get("errorCode"));
				} else if (status == 200) {
					Map<String, String> values = (Map<String, String>) m.get("response");
					for (Map.Entry<String, String> entry : values.entrySet()) {
						byte[] responseData = CommonUtil.decodeURLSafeBase64(entry.getValue());
						if (expectedCode.equalsIgnoreCase(TargetFormatCode.IMAGE_JPEG.getCode())) {
							assertEquals(true, isJPEG(responseData));
						} else if (expectedCode.equalsIgnoreCase(TargetFormatCode.IMAGE_PNG.getCode())) {
							assertEquals(true, isPNG(responseData));
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail();
		}
	}

	/**
	 * Checks if the provided byte array represents a JPEG image.
	 *
	 * Some Extra info about other file format with jpeg: initial of file contains
	 * these bytes BMP : 42 4D JPG : FF D8 FF EO ( Starting 2 Byte will always be
	 * same) PNG : 89 50 4E 47 GIF : 47 49 46 38 When a JPG file uses JFIF or EXIF,
	 * The signature is different : Raw : FF D8 FF DB JFIF : FF D8 FF E0 EXIF : FF
	 * D8 FF E1
	 * 
	 * @param imageData The byte array containing image data.
	 * @return true if the data represents a JPEG image, false otherwise.
	 * @throws Exception If there is an error while reading the data.
	 */
	public static Boolean isJPEG(byte[] imageData) throws Exception {
		DataInputStream ins = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(imageData)));
		try {
			if (ins.readInt() == 0xffd8ffe0) {
				return true;
			} else {
				return false;

			}
		} finally {
			ins.close();
		}
	}

	/**
	 * Checks if the provided byte array represents a PNG image.
	 *
	 * @param imageData The byte array containing image data.
	 * @return true if the data represents a PNG image, false otherwise.
	 * @throws Exception If there is an error while reading the data.
	 */
	public static Boolean isPNG(byte[] imageData) throws Exception {
		DataInputStream ins = new DataInputStream(new BufferedInputStream(new ByteArrayInputStream(imageData)));
		try {
			if (ins.readInt() == 0x89504e47) {
				return true;
			} else {
				return false;

			}
		} finally {
			ins.close();
		}
	}
}