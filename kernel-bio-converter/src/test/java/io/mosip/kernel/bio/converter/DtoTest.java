package io.mosip.kernel.bio.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import io.mosip.kernel.bio.converter.dto.ConvertRequestDto;

class DtoTest {
	@Test
    void testConvertRequestDtoEquals() {
        Map<String, String> values1 = new HashMap<>();
        values1.put("key1", "value1");
        Map<String, String> values2 = new HashMap<>();
        values2.put("key1", "value1");

        ConvertRequestDto dto1 = new ConvertRequestDto();
        dto1.setValues(values1);
        dto1.setSourceFormat("source");
        dto1.setTargetFormat("target");
        dto1.setSourceParameters(new HashMap<>());
        dto1.setTargetParameters(new HashMap<>());

        ConvertRequestDto dto2 = new ConvertRequestDto();
        dto2.setValues(values2);
        dto2.setSourceFormat("source");
        dto2.setTargetFormat("target");
        dto2.setSourceParameters(new HashMap<>());
        dto2.setTargetParameters(new HashMap<>());

        assertEquals(dto1, dto2); // Should be equal
    }

    @Test
    void testConvertRequestDtoNotEquals() {
        ConvertRequestDto dto1 = new ConvertRequestDto();
        dto1.setSourceFormat("source1");
        dto1.setTargetFormat("target");
        
        ConvertRequestDto dto2 = new ConvertRequestDto();
        dto2.setSourceFormat("source2"); // Different source format
        dto2.setTargetFormat("target");

        assertNotEquals(dto1, dto2); // Should not be equal
    }

    @Test
    void testConvertRequestDtoHashCode() {
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");

        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setValues(values);
        dto.setSourceFormat("source");
        dto.setTargetFormat("target");

        int expectedHashCode = Objects.hash(values, "source", "target", null, null);
        assertEquals(expectedHashCode, dto.hashCode());
    }

    @Test
    void testConvertRequestDtoCanEqual() {
        ConvertRequestDto dto = new ConvertRequestDto();
        assertTrue(dto.canEqual(new ConvertRequestDto())); // Can equal another instance
        assertFalse(dto.canEqual(new Object())); // Cannot equal a non-instance
    }
    
    @Test
    void testConvertRequestDtoToString() {
        Map<String, String> values = new HashMap<>();
        values.put("key1", "value1");
        
        ConvertRequestDto dto = new ConvertRequestDto();
        dto.setValues(values);
        dto.setSourceFormat("source");
        dto.setTargetFormat("target");
        dto.setSourceParameters(new HashMap<>());
        dto.setTargetParameters(new HashMap<>());

		assertTrue(dto.toString().contains("ConvertRequestDto(values="));
    }
}