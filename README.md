# UltraOCR SDK Java

UltraOCR SDK for Java.

[UltraOCR](https://ultraocr.com.br/) is a platform that assists in the document analysis process with AI.

For more details about the system, types of documents, routes and params, access our [documentation](https://docs.nuveo.ai/ocr/v2/).

## Instalation

First of all, you must install this package with mvn, adding this dependency to your `pom.xml`:

```
<dependency>
  <groupId>com.nuveo.ultraocr</groupId>
  <artifactId>ultraocr</artifactId>
  <version>1.0.2</version>
</dependency>
```

Then you must import the UltraOCR SDK in your code with:

```java
import com.nuveo.ultraocr.Client;
```

## Step by step

### First step - Client Creation and Authentication

With the UltraOCR SDK installed and imported, the first step is create the Client and authenticate, you have two ways to do it.

The first one, you can do it in two steps with:

```java
Client client = new Client();
client.authenticate("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET", 60);
```

The third argument on `authenticate` function is `expires`, a long between `1` and `1440`, the Token time expiration in minutes.

Another way is creating the client with `AutoRefresh` option. As example:

```java
Client client = new Client("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET", 60);
```

or:

```java
Client client = new Client();
client.setAutoRefresh("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET", 60);
```

The Client have following allowed setters:

- `setAutoRefresh`: Set client to be auto refreshed.
- `setAuthBaseUrl`: Change the base url to authenticate (Default UltraOCR url).
- `setBaseUrl`: Change the base url to send documents (Default UltraOCR url).
- `setTimeout`: Change the pooling timeout in seconds (Default 30).
- `setInterval`: Change the pooling interval in seconds (Default 1).
- `setHttpClient`: Change http client to do requests on UltraOCR (Default newHttpClient()).

### Second step - Send Documents

With everything set up, you can send documents:

```java
HashMap<String,String> params = new HashMap<>();
HashMap<String,Object> metadata = new HashMap<>();
List<Map<String,Object>> batchMetadata = new ArrayList<>();
client.sendJob("SERVICE", "FILE_PATH", metadata, params); // Simple job
client.sendBatch("SERVICE", "FILE_PATH", batchMetadata, params); // Simple batch
client.sendJobBase64("SERVICE", "BASE64_DATA", metadata, params); // Job in base64
client.sendBatchBase64("SERVICE", "BASE64_DATA", batchMetadata, params); // Batch in base64
client.sendJobSingleStep("SERVICE", "BASE64_DATA", metadata, params); // Job in base64, faster, but with limits
```

Send batch response example:

```java
{
  id: "0ujsszwN8NRY24YaXiTIE2VWDTS",
  statusUrl: "https://ultraocr.apis.nuveo.ai/v2/ocr/batch/status/0ujsszwN8NRY24YaXiTIE2VWDTS"
}
```

Send job response example:

```java
{
  id: "0ujsszwN8NRY24YaXiTIE2VWDTS",
  statusUrl: "https://ultraocr.apis.nuveo.ai/v2/ocr/job/result/0ujsszwN8NRY24YaXiTIE2VWDTS"
}
```

For jobs, to send facematch file (if requested on query params or using facematch service) or extra file (if requested on query params) to send job with document back side you must pass the files info after main document file.

Examples using CNH service and sending facematch and/or extra files:

```java
// jobs with only extra document
params.put("extra-document", "true");
client.sendJob("cnh", "FILE_PATH", "", "EXTRA_FILE_PATH", metadata, params);
client.sendJobBase64("cnh", "BASE64_DATA", "", "EXTRA_BASE64_DATA", metadata, params);
client.sendJobSingleStep("cnh", "BASE64_DATA", "", "EXTRA_BASE64_DATA", metadata, params);

// jobs with facematch and extra document
params.put("facematch", "true");
client.sendJob("cnh", "FILE_PATH", "FACEMATCH_FILE_PATH", "EXTRA_FILE_PATH", metadata, params);
client.sendJobBase64("cnh", "BASE64_DATA", "FACEMATCH_BASE64_DATA", "EXTRA_BASE64_DATA", metadata, params);
client.sendJobSingleStep("cnh", "BASE64_DATA", "FACEMATCH_BASE64_DATA", "EXTRA_BASE64_DATA", metadata, params);

// jobs with only extra document
params.put("extra-document", "false");
client.sendJob("cnh", "FILE_PATH", "FACEMATCH_FILE_PATH", "", metadata, params);
client.sendJobBase64("cnh", "BASE64_DATA", "FACEMATCH_BASE64_DATA", "", metadata, params);
client.sendJobSingleStep("cnh", "BASE64_DATA", "FACEMATCH_BASE64_DATA", "", metadata, params);
```

Alternatively, you can request the signed url directly, without any utility, but you will must to upload the document manually. Example:

```java
import com.nuveo.ultraocr.enums.Resource;

SignedUrlResponse response = client.generateSignedUrl("SERVICE", Resource.JOB, metadata, params); // Request job
Map<String, String> urls = response.getUrls();
String url = urls.get("document");
Path path = Path.of(filePath);
byte[] file = Files.readAllBytes(path);
// PUT to url with file on body

SignedUrlResponse response = client.generateSignedUrl("SERVICE", Resource.BATCH, batchMetadata, params); // Request batch
Map<String, String> urls = response.getUrls();
String url = urls.get("document");
Path path = Path.of(filePath);
byte[] file = Files.readAllBytes(path);
// PUT to url with file on body
```

Example of response from `generateSignedUrl` with facematch and extra files:

```java
{
  exp: "60000",
  id: "0ujsszwN8NRY24YaXiTIE2VWDTS",
  statusUrl: "https://ultraocr.apis.nuveo.ai/v2/ocr/batch/status/0ujsszwN8NRY24YaXiTIE2VWDTS",
  urls: {
    "document": "https://presignedurldemo.s3.eu-west-2.amazonaws.com/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJJWZ7B6WCRGMKFGQ%2F20180210%2Feu-west-2%2Fs3%2Faws4_request&X-Amz-Date=20180210T171315Z&X-Amz-Expires=1800&X-Amz-Signature=12b74b0788aa036bc7c3d03b3f20c61f1f91cc9ad8873e3314255dc479a25351&X-Amz-SignedHeaders=host",
    "selfie": "https://presignedurldemo.s3.eu-west-2.amazonaws.com/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJJWZ7B6WCRGMKFGQ%2F20180210%2Feu-west-2%2Fs3%2Faws4_request&X-Amz-Date=20180210T171315Z&X-Amz-Expires=1800&X-Amz-Signature=12b74b0788aa036bc7c3d03b3f20c61f1f91cc9ad8873e3314255dc479a25351&X-Amz-SignedHeaders=host",
    "extra_document": "https://presignedurldemo.s3.eu-west-2.amazonaws.com/image.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAJJWZ7B6WCRGMKFGQ%2F20180210%2Feu-west-2%2Fs3%2Faws4_request&X-Amz-Date=20180210T171315Z&X-Amz-Expires=1800&X-Amz-Signature=12b74b0788aa036bc7c3d03b3f20c61f1f91cc9ad8873e3314255dc479a25351&X-Amz-SignedHeaders=host"
  }
}
```

### Third step - Get Result

With the job or batch id, you can get the job result or batch status with:

```java
BatchStatusResponse response = client.getBatchStatus("BATCH_ID"); // Batches
JobResultResponse jobResponse = client.getJobResult("JOB_ID", "JOB_ID"); // Simple jobs
JobResultResponse jobResponse2 = client.getJobResult("BATCH_ID", "JOB_ID"); // Jobs belonging to batches
List<BatchResultJob> batchResult = client.getBatchResult("BATCH_ID"); // Get batch jobs result as array
BatchResultStorageResponse storage = client.getBatchResultStorage("BATCH_ID", params); // Get batch jobs result in a file

// More details about job and batch
BatchInfoResponse batchInfo = client.getBatchInfo("BATCH_ID"); // Batches info (without jobs info)
JobInfoResponse jobInfo = client.getJobInfo("JOB_ID"); // Jobs info (single jobs only)
```

Alternatively, you can use a utily `waitForJobDone` or `waitForBatchDone`:

```java
BatchStatusResponse response = client.waitForBatchDone("BATCH_ID", true); // Batches, ends when the batch and all it jobs are finished
BatchStatusResponse response2 = client.waitForBatchDone("BATCH_ID", false); // Batches, ends when the batch is finished
JobResultResponse jobResponse = client.waitForJobDone("JOB_ID", "JOB_ID"); // Simple jobs
JobResultResponse jobResponse2 = client.waitForJobDone("BATCH_ID", "JOB_ID"); // Jobs belonging to batches
```

Batch status example:

```java
{
  batchKsuid: "2AwrSd7bxEMbPrQ5jZHGDzQ4qL3",
  createdAt: "2022-06-22T20:58:09Z",
  jobs: [
    {
      "created_at": "2022-06-22T20:58:09Z",
      "job_ksuid": "0ujsszwN8NRY24YaXiTIE2VWDTS",
      "result_url": "https://ultraocr.apis.nuveo.ai/v2/ocr/job/result/2AwrSd7bxEMbPrQ5jZHGDzQ4qL3/0ujsszwN8NRY24YaXiTIE2VWDTS",
      "status": "processing"
    }
  ],
  service: "cnh",
  status: "done"
}
```

Job result example:

```java
{
  created_at: "2022-06-22T20:58:09Z",
  jobKsuid: "2AwrSd7bxEMbPrQ5jZHGDzQ4qL3",
  result: {
    "Time": "7.45",
    "Document": [
      {
        "Page": 1,
        "Data": {
          "DocumentType": {
            "conf": 99,
            "value": "CNH"
          }
        }
      }
    ]
  },
  service: "idtypification",
  status: "done"
}
```

### Simplified way

You can do all steps in a simplified way, with `createAndWaitJob` or `createAndWaitBatch` utilities:

```java
import com.nuveo.ultraocr.Client;

Client client = new Client("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET", 60);
JobResultResponse response = client.createAndWaitJob("SERVICE", "FILE_PATH", metadata, params); // simple job
JobResultResponse response2 = client.createAndWaitJob("cnh", "FILE_PATH", "FACEMATCH_FILE_PATH", "EXTRA_FILE_PATH", metadata, params); // job with facematch and extra file
```

Or:

```java
import com.nuveo.ultraocr.Client;

Client client = new Client("YOUR_CLIENT_ID", "YOUR_CLIENT_SECRET", 60);
BatchStatusResponse response = client.createAndWaitBatch("SERVICE", "FILE_PATH", batchMetadata, params, false);
```

The `createAndWaitJob` has the `sendJob` arguments and `getJobResult` response, while the `createAndWaitBatch` has the `sendBatch` arguments with the addition of `waitJobs` as last parameter and has the `getBatchStatus` response.

### Get many results

You can get all jobs in a given interval by calling `getJobs` utility:

```java
List<JobResultResponse> jobs = client.getJobs("START_DATE", "END_DATE") // Dates in YYYY-MM-DD format
```

Results:

```java
[
  {
    createdAt: "2022-06-22T20:58:09Z",
    jobKsuid: "2AwrSd7bxEMbPrQ5jZHGDzQ4qL3",
    result: {
      "Time": "7.45",
      "Document": [
        {
          "Page": 1,
          "Data": {
            "DocumentType": {
              "conf": 99,
              "value": "CNH"
            }
          }
        }
      ]
    },
    service: "idtypification",
    status: "done"
  },
  {
    createdAt: "2022-06-22T20:59:09Z",
    jobKsuid: "2AwrSd7bxEMbPrQ5jZHGDzQ4qL4",
    result: {
      "Time": "8.45",
      "Document": [
        {
          "Page": 1,
          "Data": {
            "DocumentType": {
              "conf": 99,
              "value": "CNH"
            }
          }
        }
      ]
    },
    service: "cnh",
    status: "done"
  },
  {
    createdAt: "2022-06-22T20:59:39Z",
    jobKsuid: "2AwrSd7bxEMbPrQ5jZHGDzQ4qL5",
    service: "cnh",
    status: "processing"
  }
]
```
