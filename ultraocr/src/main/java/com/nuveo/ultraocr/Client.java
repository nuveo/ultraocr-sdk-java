package com.nuveo.ultraocr;

import com.nuveo.ultraocr.response.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class Client {
    private String clientID;
    private String clientSecret;
    private String baseUrl;
    private String authBaseUrl;
    private String token;
    private Instant expiresAt;
    private int expires;
    private int timeout;
    private int interval;
    private boolean autoRefresh;
    private HttpClient httpClient;

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

    public void setAutoRefresh(String clientID, String clientSecret, int expires) {
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

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setInterval(int interval) {
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

    public void authenticate(String clientID, String clientSecret, int expires)
            throws IOException, InterruptedException {
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

        TokenResponse responseBody = gson.fromJson(response.body(), TokenResponse.class);
        this.token = responseBody.getToken();
    }

    private void autoAuthenticate() throws IOException, InterruptedException {
        if (this.autoRefresh && !Instant.now().isBefore(this.expiresAt)) {
            this.authenticate(this.clientID, this.clientSecret, this.expires);
        }
    }

    private String post(String url, Object data, Map<String, String> params)
            throws IOException, InterruptedException {
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

        HttpResponse<String> response = this.httpClient.send(request, BodyHandlers.ofString());

        return response.body();
    }

    private String get(String url, Map<String, String> params)
            throws IOException, InterruptedException {
        this.autoAuthenticate();
        URI uri = URI.create(getFullUrl(url, params));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header(Constants.HEADER_ACCEPT, Constants.APPLICATION_JSON)
                .header(Constants.HEADER_AUTHORIZATION, Constants.BEARER_PREFIX + this.token)
                .GET()
                .build();

        HttpResponse<String> response = this.httpClient.send(request, BodyHandlers.ofString());

        return response.body();
    }

    public SignedUrlResponse generateSignedUrl(String service, String resource, Object metadata,
            Map<String, String> params) throws IOException, InterruptedException {
        String url = String.format("%s/ocr/%s/%s", this.baseUrl, resource, service);
        String response = this.post(url, metadata, params);
        Gson gson = new Gson();
        return gson.fromJson(response, SignedUrlResponse.class);
    }

    public BatchStatusResponse getBatchStatus(String batchKsuid) throws IOException, InterruptedException {
        String url = String.format("%s/ocr/batch/status/%s", this.baseUrl, batchKsuid);
        String response = this.get(url, null);
        Gson gson = new Gson();
        return gson.fromJson(response, BatchStatusResponse.class);
    }

    public JobResultResponse getJobResult(String batchKsuid, String jobKsuid) throws IOException, InterruptedException {
        String url = String.format("%s/ocr/job/result/%s/%s", this.baseUrl, batchKsuid, jobKsuid);
        String response = this.get(url, null);
        Gson gson = new Gson();
        return gson.fromJson(response, JobResultResponse.class);
    }
}