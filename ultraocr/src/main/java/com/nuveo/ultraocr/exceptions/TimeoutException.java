package com.nuveo.ultraocr.exceptions;

public class TimeoutException extends Exception {
    public TimeoutException(long timeout) {
        super(String.format("timeout reached after %d seconds", timeout));
    }
}
