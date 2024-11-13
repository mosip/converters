package io.mosip.kernel.bio.converter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = KernelBioConverterApplication.class)
class KernelBioConverterApplicationTest {

    /**
     * Test if the Spring Boot application context loads without any exceptions.
     */
    @Test
    void contextLoads() {
        assertDoesNotThrow(() -> KernelBioConverterApplication.main(new String[] {}));
    }
}