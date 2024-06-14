# Converters

This project has the implementation for biometric and document converters.

## Channel:
Default channel is HTTP (POST)
## Request:
```
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

| Property | Description |
| -------- | ------------|
| values | key-value pairs, with base64 url encoded data |
| sourceFormat | Http mime types, ISO formats |
| sourceParameters | key-value pairs |
| targetFormat |  Http mime types, ISO formats |
| targetParameters | key-value pairs |

## Response:
```
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

| Property | Description |
| -------- | ------------|
| response | key-value pairs, with base64 url encoded converted data |

## Error-codes:

|     Code    |          Description                  	                                                    |
| ------------| --------------------------------------------------------------------------------------------|
| MOS-CNV-001 | Input Source Request may be null or Source Format may be null or Target Format may be null	|
| MOS-CNV-002 | Invalid Request Value	                                                                    |
| MOS-CNV-003 | Invalid Source Value or Source Format not supported					                        |
| MOS-CNV-004 | Invalid Target Value or Target Format not supported					                        |
| MOS-CNV-005 | Source value can not be empty or null					                                    |
| MOS-CNV-006 | Source not valid base64urlencoded					                                        |
| MOS-CNV-007 | Could not read Source ISO Image Data				                                        |
| MOS-CNV-008 | Source not valid ISO ISO19794_4_2011				                                        |
| MOS-CNV-009 | Source not valid ISO ISO19794_5_2011					                                    |
| MOS-CNV-010 | Source not valid ISO ISO19794_6_2011					                                    |
| MOS-CNV-011 | Target format not valid 																	|
| MOS-CNV-500 | Technical Error																				|