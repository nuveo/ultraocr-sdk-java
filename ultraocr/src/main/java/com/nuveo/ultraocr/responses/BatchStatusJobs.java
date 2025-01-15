package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

public class BatchStatusJobs {
    @SerializedName("job_ksuid")
    private String jobKsuid;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("result_url")
    private String resultUrl;

    private String status;
    private String error;

    public String getJobKsuid() {
        return jobKsuid;
    }

    public void setJobKsuid(String jobKsuid) {
        this.jobKsuid = jobKsuid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public String getResultUrl() {
        return resultUrl;
    }

    public void setResultUrl(String resultUrl) {
        this.resultUrl = resultUrl;
    }
}