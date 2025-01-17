package com.nuveo.ultraocr;

import com.nuveo.ultraocr.responses.*;
import com.nuveo.ultraocr.enums.*;
import com.nuveo.ultraocr.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.Gson;

/**
* Client to help on UltraOCR usage. For more details about all arguments and returns,
* access the oficial system documentation on https://docs.nuveo.ai/ocr/v2/.
*/
public class Client {
    private String clientID;
    private String clientSecret;
    private String baseUrl;
    private String authBaseUrl;
    private String token;
    private Instant expiresAt;
    private long expires;
    private long timeout;
    private long interval;
    private boolean autoRefresh;
    private HttpClient httpClient;

    /** 
    * Class constructor.
    */
    public Client() {
        this.authBaseUrl = Constants.AUTH_BASE_URL;
        this.baseUrl = Constants.BASE_URL;
        this.timeout = Constants.API_TIMEOUT;
        this.interval = Constants.POOLING_INTERVAL;
        this.expires = Constants.DEFAULT_EXPIRATION_TIME;
        this.autoRefresh = false;
        this.clientID = "";
        this.clientSecret = "";
        this.token = "";
        this.expiresAt = Instant.now();
        this.httpClient = HttpClient.newHttpClient();
    }

    public void setAutoRefresh(String clientID, String clientSecret, long expires) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.expires = expires;
        this.autoRefresh = true;
        this.expiresAt = Instant.now();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setAuthBaseUrl(String authBaseUrl) {
        this.authBaseUrl = authBaseUrl;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private static String getFullUrl(String url, Map<String, String> params) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }

            builder.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
        }

        return url + "?" + builder.toString();
    }

    private static void validateStatus(int expected, int got) throws InvalidStatusCodeException {
        if (expected != got) {
            throw new InvalidStatusCodeException(expected, got);
        }
    }

    public void authenticate(String clientID, String clientSecret, long expires)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        Map<Object, Object> data = new HashMap<>();
        data.put("ClientID", clientID);
        data.put("ClientSecret", clientSecret);
        data.put("ExpiresIn", expires);

        Gson gson = new Gson();
        String body = gson.toJson(data);

        String url = String.format("%s/token", this.authBaseUrl);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(Constants.HEADER_ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.APPLICATION_JSON)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        HttpResponse<String> response = this.httpClient.send(request, BodyHandlers.ofString());
        validateStatus(Constants.STATUS_OK, response.statusCode());

        TokenResponse responseBody = gson.fromJson(response.body(), TokenResponse.class);
        this.token = responseBody.getToken();
    }

    private void autoAuthenticate() throws IOException, InterruptedException, InvalidStatusCodeException {
        if (this.autoRefresh && !Instant.now().isBefore(this.expiresAt)) {
            this.authenticate(this.clientID, this.clientSecret, this.expires);
        }
    }

    private HttpResponse<String> post(String url, Object data, Map<String, String> params)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        this.autoAuthenticate();
        Gson gson = new Gson();
        String body = gson.toJson(data);

        URI uri = URI.create(getFullUrl(url, params));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(Constants.HEADER_ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_AUTHORIZATION, Constants.BEARER_PREFIX + this.token)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return this.httpClient.send(request, BodyHandlers.ofString());
    }

    private HttpResponse<String> get(String url, Map<String, String> params)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        this.autoAuthenticate();
        URI uri = URI.create(getFullUrl(url, params));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(Constants.HEADER_ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_AUTHORIZATION, Constants.BEARER_PREFIX + this.token)
                .GET()
                .build();

        return this.httpClient.send(request, BodyHandlers.ofString());
    }

    public SignedUrlResponse generateSignedUrl(String service, Resource resource, Object metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        String url = String.format("%s/ocr/%s/%s", this.baseUrl, resource, service);
        HttpResponse<String> response = this.post(url, metadata, params);
        validateStatus(Constants.STATUS_OK, response.statusCode());
        Gson gson = new Gson();
        return gson.fromJson(response.body(), SignedUrlResponse.class);
    }

    public BatchStatusResponse getBatchStatus(String batchKsuid)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        String url = String.format("%s/ocr/batch/status/%s", this.baseUrl, batchKsuid);
        HttpResponse<String> response = this.get(url, null);
        validateStatus(Constants.STATUS_OK, response.statusCode());
        Gson gson = new Gson();
        return gson.fromJson(response.body(), BatchStatusResponse.class);
    }

    public JobResultResponse getJobResult(String batchKsuid, String jobKsuid)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        String url = String.format("%s/ocr/job/result/%s/%s", this.baseUrl, batchKsuid, jobKsuid);
        HttpResponse<String> response = this.get(url, null);
        validateStatus(Constants.STATUS_OK, response.statusCode());
        Gson gson = new Gson();
        return gson.fromJson(response.body(), JobResultResponse.class);
    }

    public JobResultResponse waitForJobDone(String batchKsuid, String jobKsuid)
            throws IOException, InterruptedException, TimeoutException, InvalidStatusCodeException {
        Instant end = Instant.now().plusSeconds(this.timeout);
        JobResultResponse response;
        while (true) {
            response = this.getJobResult(batchKsuid, jobKsuid);
            String status = response.getStatus();
            if (status.equals(Constants.STATUS_DONE) || status.equals(Constants.STATUS_ERROR)) {
                return response;
            }

            if (Instant.now().isBefore(end)) {
                throw new TimeoutException(this.timeout);
            }

            Thread.sleep(this.interval * 1000);
        }
    }

    public BatchStatusResponse waitForBatchDone(String batchKsuid, boolean waitJobs)
            throws IOException, InterruptedException, TimeoutException, InvalidStatusCodeException {
        Instant end = Instant.now().plusSeconds(this.timeout);
        BatchStatusResponse response;
        while (true) {
            response = this.getBatchStatus(batchKsuid);
            String status = response.getStatus();
            if (status.equals(Constants.STATUS_DONE) || status.equals(Constants.STATUS_ERROR)) {
                break;
            }

            if (Instant.now().isBefore(end)) {
                throw new TimeoutException(this.timeout);
            }

            Thread.sleep(this.interval * 1000);
        }

        if (waitJobs) {
            for (BatchStatusJobs job : response.getJobs()) {
                waitForJobDone(response.getBatchKsuid(), job.getJobKsuid());
            }
        }

        return response;
    }

    public void uploadFile(String url, byte[] body)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header(Constants.HEADER_ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_CONTENT_TYPE, Constants.APPLICATION_JSON)
                .PUT(HttpRequest.BodyPublishers.ofByteArray(body))
                .build();
        HttpResponse<String> response = this.httpClient.send(request, BodyHandlers.ofString());
        validateStatus(Constants.STATUS_OK, response.statusCode());
    }

    public void uploadFileWithPath(String url, String filePath)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        Path path = Path.of(filePath);
        byte[] file = Files.readAllBytes(path);
        uploadFile(url, file);
    }

    public CreatedResponse sendJobSingleStep(String service, String file, Map<String, Object> metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        Map<String, Object> body = new HashMap<>();
        body.put("data", file);
        body.put("metadata", metadata);

        String url = String.format("%s/ocr/job/send/%s", this.baseUrl, service);
        HttpResponse<String> response = this.post(url, metadata, params);
        validateStatus(Constants.STATUS_OK, response.statusCode());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), CreatedResponse.class);
    }

    public CreatedResponse sendJobSingleStep(String service, String file, String facematchFile, String extraFile,
            Map<String, Object> metadata, Map<String, String> params)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        Map<String, Object> body = new HashMap<>();
        body.put("data", file);
        body.put("metadata", metadata);

        if (params.containsKey(Constants.KEY_FACEMATCH)
                && params.get(Constants.KEY_FACEMATCH).equals(Constants.FLAG_TRUE)) {
            body.put(Constants.KEY_FACEMATCH, facematchFile);
        }

        if (params.containsKey(Constants.KEY_EXTRA) && params.get(Constants.KEY_EXTRA).equals(Constants.FLAG_TRUE)) {
            body.put("extra", extraFile);
        }

        String url = String.format("%s/ocr/job/send/%s", this.baseUrl, service);
        HttpResponse<String> response = this.post(url, metadata, params);
        validateStatus(Constants.STATUS_OK, response.statusCode());

        Gson gson = new Gson();
        return gson.fromJson(response.body(), CreatedResponse.class);
    }

    public CreatedResponse sendJob(String service, String filePath, Map<String, Object> metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.JOB, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFileWithPath(urls.get(Constants.KEY_DOCUMENT), filePath);

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public CreatedResponse sendJob(String service, String filePath, String facematchFilePath, String extraFilePath,
            Map<String, Object> metadata, Map<String, String> params)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.JOB, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFileWithPath(urls.get(Constants.KEY_DOCUMENT), filePath);

        if (params.containsKey(Constants.KEY_FACEMATCH)
                && params.get(Constants.KEY_FACEMATCH).equals(Constants.FLAG_TRUE)) {
            this.uploadFileWithPath(urls.get("selfie"), facematchFilePath);
        }

        if (params.containsKey(Constants.KEY_EXTRA) && params.get(Constants.KEY_EXTRA).equals(Constants.FLAG_TRUE)) {
            this.uploadFileWithPath(urls.get("extra_document"), extraFilePath);
        }

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public CreatedResponse sendJobBase64(String service, String file, Map<String, Object> metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.JOB, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFile(urls.get(Constants.KEY_DOCUMENT), file.getBytes());

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public CreatedResponse sendJobBase64(String service, String file, String facematchFile, String extraFile,
            Map<String, Object> metadata, Map<String, String> params)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.JOB, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFile(urls.get(Constants.KEY_DOCUMENT), file.getBytes());

        if (params.containsKey(Constants.KEY_FACEMATCH)
                && params.get(Constants.KEY_FACEMATCH).equals(Constants.FLAG_TRUE)) {
            this.uploadFile(urls.get("selfie"), facematchFile.getBytes());
        }

        if (params.containsKey(Constants.KEY_EXTRA) && params.get(Constants.KEY_EXTRA).equals(Constants.FLAG_TRUE)) {
            this.uploadFile(urls.get("extra_document"), extraFile.getBytes());
        }

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public CreatedResponse sendBatch(String service, String filePath, Map<String, Object> metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.BATCH, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFileWithPath(urls.get(Constants.KEY_DOCUMENT), filePath);

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public CreatedResponse sendBatchBase64(String service, String file, Map<String, Object> metadata,
            Map<String, String> params) throws IOException, InterruptedException, InvalidStatusCodeException {
        SignedUrlResponse response = this.generateSignedUrl(service, Resource.BATCH, metadata, params);
        Map<String, String> urls = response.getUrls();
        this.uploadFile(urls.get(Constants.KEY_DOCUMENT), file.getBytes());

        CreatedResponse res = new CreatedResponse();
        res.setId(response.getId());
        res.setStatusUrl(response.getStatusUrl());
        return res;
    }

    public BatchStatusResponse createAndWaitBatch(String service, String filePath, Map<String, Object> metadata,
            Map<String, String> params, boolean waitJobs)
            throws IOException, InterruptedException, TimeoutException, InvalidStatusCodeException {
        CreatedResponse response = sendBatch(service, filePath, metadata, params);
        return waitForBatchDone(response.getId(), waitJobs);
    }

    public JobResultResponse createAndWaitJob(String service, String filePath, Map<String, Object> metadata,
            Map<String, String> params)
            throws IOException, InterruptedException, TimeoutException, InvalidStatusCodeException {
        CreatedResponse response = sendJob(service, filePath, metadata, params);
        return waitForJobDone(response.getId(), response.getId());
    }

    public JobResultResponse createAndWaitJob(String service, String filePath, String facematchFilePath,
            String extraFilePath, Map<String, Object> metadata,
            Map<String, String> params)
            throws IOException, InterruptedException, TimeoutException, InvalidStatusCodeException {
        CreatedResponse response = sendJob(service, filePath, facematchFilePath, extraFilePath, metadata, params);
        return waitForJobDone(response.getId(), response.getId());
    }

    /**
    * Gets the jobs in a time interval.
    * @param  start  the start time (in the format YYYY-MM-DD).
    * @param  end    the end time (in the format YYYY-MM-DD).
    * @return        a list with the jobs.
    * @see           JobResultResponse
    * @throws        InvalidStatusCodeException if status code is not 200.
    * @throws        InterruptedException if http request fail.
    * @throws        IOException if http request fail.
    */
    public List<JobResultResponse> getJobs(String start, String end)
            throws IOException, InterruptedException, InvalidStatusCodeException {
        String url = String.format("%s/ocr/job/results", this.baseUrl);
        Map<String, String> params = new HashMap<>();
        params.put("startDate", start);
        params.put("endDate", end);

        List<JobResultResponse> jobs = new ArrayList<>();
        boolean hasNextPage = true;

        while (hasNextPage) {
            HttpResponse<String> response = this.get(url, params);
            validateStatus(Constants.STATUS_OK, response.statusCode());
            Gson gson = new Gson();
            GetJobsResponse res = gson.fromJson(response.body(), GetJobsResponse.class);

            jobs.addAll(res.getJobs());
            params.put("nextPageToken", res.getNextPageToken());

            if (res.getNextPageToken().isEmpty()) {
                hasNextPage = false;
            }
        }
        return jobs;
    }
}