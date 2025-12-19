# Converters
[![Maven Package upon a push](https://github.com/mosip/converters/actions/workflows/push-trigger.yml/badge.svg?branch=release-1.3.x)](https://github.com/mosip/converters/actions/workflows/push-trigger.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mosip_kernel-bio-converter&metric=alert_status&branch=release-1.3.x&branch=release-1.3.x)

## Overview

The **Converters** is a MOSIP module that enables the conversion of ISO biometric and document data into standard image formats such as JPEG or PNG. The module exposes API endpoints for configuring and handling conversion operations.

Note: This converters module can be used both as a library and as a service.

## Features

- Conversion of ISO biometric and document data into standard image formats (JPEG, PNG)
- Exposes API endpoints for conversion operations
- Support for multiple ISO formats (ISO19794_4_2011, ISO19794_5_2011, ISO19794_6_2011)
- Library and Service usage modes

## Services

The Converters module contains the following:

1. **[Converter Service](kernel-bio-converter)** (`kernel-bio-converter`) - Service for biometric data conversion.

## Database
NA (Not applicable)

## Local Setup

The project can be set up in two ways:

1. [Local Setup (for Development or Contribution)](#local-setup-for-development-or-contribution)
2. [Local Setup with Docker (Easy Setup for Demos)](#local-setup-with-docker-easy-setup-for-demos)

### Prerequisites

Before you begin, ensure you have the following installed:

- **JDK**: Version 21.0.5
- **Maven**: Version 3.9.6

### Runtime Dependencies

Ensure the following artifacts are available in the classpath or loader path:

- `kernel-bio-converter.jar`

### Configuration

- Converters uses configuration files from the **[mosip-config repository](https://github.com/mosip/mosip-config/tree/master)**.
- Key configuration files:
    - [application-default.properties](https://github.com/mosip/mosip-config/blob/master/application-default.properties)

## Installation

### Local Setup (for Development or Contribution)

1. Clone the repository:

```text
git clone https://github.com/mosip/converters.git
cd converters
```

2. Build and install:
   Navigate to the project directory:

```text
cd kernel-bio-converter
```

3. Build the project:

```text
mvn install -DskipTests=true -Dgpg.skip=true
```

4. Run the jar locally to use the module as a service:

```text
java -Dloader.path=. \
--add-modules=ALL-SYSTEM \
--add-opens java.xml/jdk.xml.internal=ALL-UNNAMED \
--add-opens java.base/java.lang.reflect=ALL-UNNAMED \
--add-opens java.base/java.lang.stream=ALL-UNNAMED \
--add-opens java.base/java.time=ALL-UNNAMED \
--add-opens java.base/java.time.LocalDate=ALL-UNNAMED \
--add-opens java.base/java.time.LocalDateTime=ALL-UNNAMED \
--add-opens java.base/java.time.LocalDateTime.date=ALL-UNNAMED \
--add-opens java.base/jdk.internal.reflect.DirectMethodHandleAccessor=ALL-UNNAMED \
-jar target/kernel-bio-converter-*.jar
```

5. Verify Swagger is accessible at: `http://localhost:8098/v1/converter-service/swagger-ui/index.html`

6. To use the jar as library configure the module as below:

```xml
<dependency>
    <groupId>io.mosip.kernel</groupId>
    <artifactId>kernel-bio-converter</artifactId>
    <version>${kernel.bioconverter.version}</version>
    <classifier>lib</classifier>
</dependency>
```

### Local Setup with Docker (Easy Setup for Demos)

1. Navigate to the service folder:

```text
cd kernel-bio-converter
```

2. Build the Docker image:

```text
docker build -t kernel-bio-converter:latest -f Dockerfile .
```

#### Verify Installation

Check that the container is running:

```text
docker ps
```

## Deployment

### Kubernetes

To deploy Admin on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## Documentation

### API Documentation

API endpoints and Swagger documentation are available at: `http://{host}/v1/converter-service/swagger-ui/index.html`

### Usage

**Url**: `http://{host}/v1/converter-service/convert`

**Method**: POST

**Request Structure**:

```json
{
  "id": "sample-converter",
  "version": "1.0",
  "requesttime": "2022-02-22T16:46:09.499Z",
  "request": {
    "values": {
      "Left Thumb": "<base64 url encoded bdb>",
      "Right Iris": "<base64 url encoded bdb>"
    },
    "sourceFormat": "string",
    "targetFormat": "string",
    "sourceParameters": {
      "key": "value"
    },
    "targetParameters": {
      "key": "value"
    }
  }
}
```

| **Property**          | **Description**                                                                  |
| ----------------------| -------------------------------------------------------------------------------- |
| values 					      | key-value pairs, with base64 url encoded data                                    |
| sourceFormat 			    | Http mime types, ISO formats [ISO19794_4_2011, ISO19794_5_2011, ISO19794_6_2011] |
| sourceParameters 		  | key-value pairs [IMAGE/JPEG, IMAGE/PNG]                                          |
| targetFormat 			    | Http mime types, ISO formats                                                     |
| targetParameters 		  | key-value pairs                                                                  |

**Response Structure**:

```json
{
  "id": "sample-converter",
  "version": "1.0",
  "responsetime": "2022-02-22T16:46:09.513Z",
  "errors": [
    {
      "errorCode": "string",
      "errorMessage": "string"
    }
  ],
  "response": {
    "Left Thumb": "<base64 url encoded converted data>",
    "Right Iris": "<base64 url encoded converted data>"
  }
}
```

| **Property** | **Description**                                                                            |
| :--------    | :----------------------------------------------------------------------------------------- |
| response     | key-value pairs, with base64 url encoded converted data                                    |


### Error Codes

| **Code**     | **Description**                  	                                                         |
| :----------- | :------------------------------------------------------------------------------------------ |
| MOS-CNV-001  | Input Source Request may be null or Source Format may be null or Target Format may be null	 |
| MOS-CNV-002  | Invalid Request Value	                                                                     |
| MOS-CNV-003  | Invalid Source Value or Source Format not supported					                               |
| MOS-CNV-004  | Invalid Target Value or Target Format not supported					                               |
| MOS-CNV-005  | Source value can not be empty or null					                                             |
| MOS-CNV-006  | Source not valid base64urlencoded					                                                 |
| MOS-CNV-007  | Could not read Source ISO Image Data				                                                 |
| MOS-CNV-008  | Source not valid ISO ISO19794_4_2011				                                                 |
| MOS-CNV-009  | Source not valid ISO ISO19794_5_2011					                                               |
| MOS-CNV-010  | Source not valid ISO ISO19794_6_2011					                                               |
| MOS-CNV-011  | Target format not valid 																	                                   |
| MOS-CNV-500  | Technical Error																				                                     |

## Contribution & Community

- To learn how you can contribute code to this application, [click here](https://docs.mosip.io/1.2.0/community/code-contributions).
- If you have questions or encounter issues, visit the [MOSIP Community](https://community.mosip.io/) for support.
- For any GitHub issues: [Report here](https://github.com/mosip/converters/issues)

## License

This project is licensed under the [Mozilla Public License 2.0](LICENSE).