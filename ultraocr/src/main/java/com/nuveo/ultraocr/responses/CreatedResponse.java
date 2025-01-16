package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

public class CreatedResponse {
    @SerializedName("status_url")
    private String statusUrl;

    private String id;

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
}
