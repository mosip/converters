# AGENTS.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

`kernel-bio-converter` is a MOSIP (Modular Open Source Identity Platform) Spring Boot service that converts ISO biometric data (fingerprint, face, iris) into standard image formats (JPEG/PNG). It ships as both a runnable JAR and a `-lib` classifier JAR for embedding in other MOSIP services.

## Build & Test Commands

All commands run from `kernel-bio-converter/` unless otherwise noted.

**Build (skip tests and GPG signing):**
```bash
mvn install -DskipTests=true -Dgpg.skip=true
```

**Run all tests:**
```bash
mvn test -Dgpg.skip=true
```

**Run a single test class:**
```bash
mvn test -Dtest=ConverterServiceImplTest -Dgpg.skip=true
```

The surefire plugin passes the required JVM `--add-opens` flags automatically. The build uses `--enable-preview` (Java 21) both for compilation and test execution — do not remove these flags.

**Run the JAR:**
```bash
java -Dloader.path=. \
  --add-modules=ALL-SYSTEM \
  --add-opens java.xml/jdk.xml.internal=ALL-UNNAMED \
  --add-opens java.base/java.lang.reflect=ALL-UNNAMED \
  --add-opens java.base/java.time=ALL-UNNAMED \
  --enable-preview \
  -jar target/kernel-bio-converter-*.jar
```

**Docker build:**
```bash
docker build -t kernel-bio-converter:latest -f Dockerfile .
```

## Architecture

### Dual-output build

The Maven build produces two artifacts:
- **Fat JAR** (`spring-boot-maven-plugin` repackage) — runnable service.
- **Lib JAR** (`-lib` classifier, `maven-jar-plugin`) — includes only `constant/`, `dto/`, `exception/`, and `service/**`. This is the dependency consumed by other MOSIP modules.

### Request / response envelope

All HTTP interactions use MOSIP's `kernel-core` envelope types:
- Inbound: `RequestWrapper<ConvertRequestDto>` — wraps the DTO with `id`, `version`, `requesttime`, `request`.
- Outbound: `ResponseWrapper<Map<String, String>>` — wraps the result map the same way.
- `@ResponseFilter` on `ConvertController.convert()` triggers kernel-core's response-filtering interceptor that populates the envelope metadata.

### Conversion pipeline

`ConvertController` → `IConverterApi` → `ConverterServiceImpl`:

1. Decode source format string → `SourceFormatCode` enum (throws `MOS-CNV-003` if unknown).
2. Decode target format string → `TargetFormatCode` enum (throws `MOS-CNV-004` if unknown).
3. For each entry in the `values` map:
   - URL-safe Base64-decode the ISO blob.
   - Pass to the matching `convert*IsoToImageType()` method based on source code.
   - Inside each method: decode the ISO BDIR via `mosip/biometrics-util` decoders (`FingerDecoder`, `FaceDecoder`, `IrisDecoder`).
   - Decompress the embedded image (`JPEG2000` via `ImageIO`, `WSQ` via `jnbis` `WsqDecoder`).
   - Re-encode to target format (`CommonUtil.convertBufferedImageToJPEGBytes` / `toPNGBytes`).
   - URL-safe Base64-encode the result.
4. Return the map of `{original key → converted Base64 value}`.

### Supported formats

| Source (`sourceFormat`) | Description |
|---|---|
| `ISO19794_4_2011` | Finger (JP2000 lossy/lossless or WSQ) |
| `ISO19794_5_2011` | Face (JP2000 lossy/lossless only) |
| `ISO19794_6_2011` | Iris (MONO_JPEG2000 only) |

| Target (`targetFormat`) | Description |
|---|---|
| `IMAGE/JPEG` | Plain JPEG image |
| `IMAGE/PNG` | Plain PNG image |
| `ISO19794_4_2011/JPEG`, `/PNG` | Finger ISO re-wrapped with JPEG/PNG |
| `ISO19794_5_2011/JPEG`, `/PNG` | Face ISO re-wrapped |
| `ISO19794_6_2011/PNG` | Iris ISO re-wrapped (JPEG variant intentionally disabled) |

### Security

`SecurityConfig` disables HTTP Basic auth and CSRF and permits all requests. Authentication is handled upstream by MOSIP's `kernel-auth-adapter` (Keycloak/token-based); the local `SecurityConfig` explicitly excludes the adapter's own `SecurityConfig` via a `@ComponentScan` filter in `KernelBioConverterApplication`.

### Configuration

Runtime properties are served from a Spring Cloud Config server (`mosip-config` repository). The `application.properties` in this module only contains auth-adapter bootstrap properties (`mosip.auth.adapter.impl.basepackage`, Keycloak issuer URI, allowed audience). Tests use `application-test.properties` with the same structure pointing to a dev Keycloak endpoint.

### Error codes

All business errors are `ConversionException(errorCode, message)` caught by `ConversionExceptionAdvice` and returned as HTTP 500 with a `ResponseWrapper<ServiceError>`. Error code range: `MOS-CNV-001` through `MOS-CNV-012`, plus `MOS-CNV-500` for unexpected exceptions.

### Sonar / coverage exclusions

The `sonar` Maven profile runs SonarCloud analysis. JaCoCo and Sonar both exclude `constant/`, `config/`, `dto/`, `exception/`, and the main application class from coverage metrics. Keep this in mind when writing new code in those packages.

## API

**POST** `http://{host}/v1/converter-service/convert`

Swagger UI: `http://{host}/v1/converter-service/swagger-ui/index.html`

Example request body:
```json
{
  "id": "mosip.converter",
  "version": "1.0",
  "requesttime": "2024-01-01T00:00:00.000Z",
  "request": {
    "values": {
      "Left IndexFinger": "<base64url-encoded ISO blob>"
    },
    "sourceFormat": "ISO19794_4_2011",
    "targetFormat": "IMAGE/JPEG",
    "sourceParameters": {},
    "targetParameters": {}
  }
}
```

## Library Usage (Maven)

```xml
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-bio-converter</artifactId>
    <version>${kernel.bioconverter.version}</version>
    <classifier>lib</classifier>
</dependency>
```

Call `IConverterApi.convert(values, sourceFormat, targetFormat, sourceParameters, targetParameters)` directly.