package com.nuveo.ultraocr;

public class Constants {
    public static final int STATUS_OK = 200;
    public static final int POOLING_INTERVAL = 1;
    public static final int API_TIMEOUT = 30;
    public static final int DEFAULT_EXPIRATION_TIME = 60;
    public static final String BASE_URL = "https://ultraocr.apis.nuveo.ai/v2";
    public static final String AUTH_BASE_URL = "https://auth.apis.nuveo.ai/v2";
    public static final String STATUS_DONE = "done";
    public static final String STATUS_ERROR = "error";
    public static final String APPLICATION_JSON = "application/json";
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String KEY_DOCUMENT = "document";
    public static final String KEY_FACEMATCH = "facematch";
    public static final String KEY_EXTRA = "extra-document";
    public static final String KEY_SELFIE = "selfie";
    public static final String KEY_EXTRA_URL = "extra_document";
    public static final String FLAG_TRUE = "true";
    public static final String BASE64_ATTRIBUTE = "base64";

    private Constants() {

    }
}
