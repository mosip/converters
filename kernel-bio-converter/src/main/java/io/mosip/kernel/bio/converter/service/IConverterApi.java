package io.mosip.kernel.bio.converter.service;

import java.util.Map;

import io.mosip.kernel.bio.converter.exception.ConversionException;

/**
 * Converter API Interface for the services.
 * 
 * @author Janardhan B S
 * @since 1.0.0
 */
public interface IConverterApi {
	  /**
	  *
	  * @param values Base64 URL encoded values with identifier keys.
	  *              Eg: {
	  *                     "Left MiddleFinger" : "<base64 URL encoded BDB>",
	  *                     "Left LittleFinger" : "<base64 URL encoded BDB>"
	  *                   },
	  *                 sourceFormat : "ISO19794_4_2011"                               
	  *
	  *               Eg:  { "Face" : "<base64 URL encoded BDB>" }, 
	  *                 sourceFormat : "ISO19794_5_2011"
	  *                       
	  *                 Eg:  { "Left" : "<base64 URL encoded BDB>" } , 
	  *                     sourceFormat : "ISO19794_6_2011"
	  *
	  *               Eg: { "dateOfBirth" : "<base64 URL encoded date>" }
	  *               , sourceFormat : "YYYY/mm/DD"
	  *
	  *               Eg: { "proofOfException" : "<base64 URL encoded data>" }, sourceFormat : "application/pdf"
	  *
	  * @param sourceFormat input value mime type, if not supported, ConversionException is thrown
	  * @param targetFormat output value mime type, if not supported, ConversionException is thrown
	  * @param sourceParameters Provided source value/format related parameters to be considered during conversion. Unknown parameters are ignored.
	  * @param targetParameters parameters to be considered during conversion to target format. Unknown parameters are ignored.
	  * @return converted Base64 URL encoded values w.r.t targetFormat for the input identifier keys
	  * @throws ConversionException
	  *
	  * ErrorCodes:
	  * MOS-CNV-001 Input source exception (conversion source format not supported)
	  * MOS-CNV-002 Invalid request value
	  * MOS-CNV-003 Invalid source exception (invalid source format)
	  * MOS-CNV-004 Invalid target exception (invalid target format)
	  * MOS-CNV-005 Source value can not be empty or null
	  * MOS-CNV-006 Source not valid base64urlencoded
	  * MOS-CNV-007 Could not read Source ISO Image Data
	  * MOS-CNV-008 Source not valid ISO ISO19794_4_2011 (finger)
	  * MOS-CNV-009 Source not valid ISO ISO19794_5_2011 (face)
	  * MOS-CNV-010 Source not valid ISO ISO19794_6_2011 (iris)
	  * MOS-CNV-011 Target format exception (conversion to target format failed)
	  * MOS-CNV-012 Not Supported Compression Type
	  * MOS-CNV-500 Technical Error (unexpected exception)
	  */
	  Map<String, String> convert(Map<String, String> values, String sourceFormat, String targetFormat, Map<String, String> sourceParameters, Map<String, String> targetParameters) throws ConversionException;	  
	}