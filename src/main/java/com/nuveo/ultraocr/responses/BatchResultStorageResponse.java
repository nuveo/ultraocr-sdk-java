package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchResultStorageResponse {
    private String exp;
    private String url;

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}