package com.nuveo.ultraocr.response;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class SignedUrlResponse {
    @SerializedName("status_url")
    private String statusUrl;

    private String id;
    private int expires;
    private Map<String, String> urls;

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusUrl() {
        return statusUrl;
    }

    public void setStatusUrl(String statusUrl) {
        this.statusUrl = statusUrl;
    }

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }
}
