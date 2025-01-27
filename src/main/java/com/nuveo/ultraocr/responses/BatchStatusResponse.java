package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BatchStatusResponse {
    @SerializedName("batch_ksuid")
    private String batchKsuid;

    @SerializedName("created_at")
    private String createdAt;

    private String service;
    private String status;
    private String error;
    private List<BatchStatusJobs> jobs;

    public String getBatchKsuid() {
        return batchKsuid;
    }

    public void setBatchKsuid(String batchKsuid) {
        this.batchKsuid = batchKsuid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<BatchStatusJobs> getJobs() {
        return jobs;
    }

    public void setJobs(List<BatchStatusJobs> jobs) {
        this.jobs = jobs;
    }
}