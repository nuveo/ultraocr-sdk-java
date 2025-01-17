package com.nuveo.ultraocr.exceptions;

public class InvalidStatusCodeException extends Exception {
    public InvalidStatusCodeException(int expected, int got) {
        super(String.format("expected %d status code, got: %d", expected, got));
    }
}
