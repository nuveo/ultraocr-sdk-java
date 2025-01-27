package com.nuveo.ultraocr.responses;

import java.util.List;

public class GetJobsResponse {
    private String nextPageToken;
    private List<JobResultResponse> jobs;

    public List<JobResultResponse> getJobs() {
        return jobs;
    }

    public void setJobs(List<JobResultResponse> jobs) {
        this.jobs = jobs;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }
}