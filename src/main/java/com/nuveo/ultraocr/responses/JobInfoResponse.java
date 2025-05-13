package com.nuveo.ultraocr.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobInfoResponse {
    @SerializedName("job_id")
    private String jobId;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("company_id")
    private String companyId;

    @SerializedName("validation_id")
    private String validationId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("finished_at")
    private String finishedAt;

    @SerializedName("client_data")
    private Object clientData;

    @SerializedName("validation_status")
    private String validationStatus;

    private String service;
    private String status;
    private String error;
    private Object validation;
    private Object metadata;
    private Result result;
    private String source;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getValidationId() {
        return validationId;
    }

    public void setValidationId(String validationId) {
        this.validationId = validationId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Object getClientData() {
        return clientData;
    }

    public void setClientData(Object clientData) {
        this.clientData = clientData;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
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

    public Object getValidation() {
        return validation;
    }

    public void setValidation(Object validation) {
        this.validation = validation;
    }

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}