package com.nuveo.ultraocr;

import java.time.Instant;

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
    }

    public void setAutoRefresh(String clientID, String clientSecret, int expires) {
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.expires = expires;
        this.autoRefresh = true;
        this.expiresAt = Instant.now().minusSeconds(1);
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
}
