package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

public class BatchResultJob {
    @SerializedName("job_ksuid")
    private String jobKsuid;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("validation_status")
    private String validationStatus;

    @SerializedName("client_data")
    private Object clientData;

    private String service;
    private String status;
    private String error;
    private String filename;
    private Object validation;
    private Result result;

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

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public Object getClientData() {
        return clientData;
    }

    public void setClientData(Object clientData) {
        this.clientData = clientData;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Object getValidation() {
        return validation;
    }

    public void setValidation(Object validation) {
        this.validation = validation;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}