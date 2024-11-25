# Converters

# Overview

This repository contains the source code for the Converters module, which enables the conversion of ISO biometric and document data into standard image formats such as JPEG or PNG. The module exposes API endpoints for configuring and handling conversion operations.

Note: This converters module can be used both as a library and as a service. 

## Build & run (for developers)

To build and run the project, ensure you have the following prerequisites installed:

- Java Development Kit (JDK): Version 21.0.3

- Apache Maven: Version 3.9.6

Follow these below steps to get started:

1. Build and install:
 - Navigate to the project directory:

    ```shell
    $ cd converters/kernel-bio-converter
    ```
2. Build the project:

```shell
    $ mvn install -DskipTests=true -Dgpg.skip=true
```
3. Run the jar locally to use the module as a service:

    ```java
    $ java -Dloader.path=.
    --add-modules=ALL-SYSTEM 
    --add-opens java.xml/jdk.xml.internal=ALL-UNNAMED 
    --add-opens java.base/java.lang.reflect=ALL-UNNAMED 
    --add-opens java.base/java.lang.stream=ALL-UNNAMED 
    --add-opens java.base/java.time=ALL-UNNAMED
    --add-opens java.base/java.time.LocalDate=ALL-UNNAMED 
    --add-opens java.base/java.time.LocalDateTime=ALL-UNNAMED 
    --add-opens java.base/java.time.LocalDateTime.date=ALL-UNNAMED 
    --add-opens java.base/jdk.internal.reflect.DirectMethodHandleAccessor=ALL-UNNAMED  -jar target/{latestjarname}.jar.
    ```

Swagger url: [Swagger UI](http://localhost:8098/v1/converter-service/swagger-ui/index.html)

4. To use the jar as library configure the module as below:

```xml
		<dependency>
			<groupId>io.mosip.kernel</groupId>
			<artifactId>kernel-bio-converter</artifactId>
			<version>${kernel.bioconverter.version}</version>
			<classifier>lib</classifier>
		</dependency>
```

# Docker Instructions

Follow these steps to build a Docker image for a specific service:

1. Navigate to the service folder:

Use the cd command to move to the directory containing the desired service's Dockerfile.

```shell
    $ cd <service folder>
```

2. Build the Docker image:

```shell
    $ docker build -t <image-name>:<tag> -f Dockerfile .
```
        
## APIs testing

Refer to the below Url

**Url**: http://{host}/v1/converter-service/convert

Note: The above Url runs locally or any host environment setup

Method: POST

Refer below for API request structure:

## Request:

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


Refer below for API response structure:


## Response:

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


## Error-codes:


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

## Deploy
To deploy Admin on Kubernetes cluster using Dockers refer to [Sandbox Deployment](https://docs.mosip.io/1.2.0/deploymentnew/v3-installation).

## License
This project is licensed under the terms of [Mozilla Public License 2.0](LICENSE).