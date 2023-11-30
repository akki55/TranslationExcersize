package org.example;

public class TranslateServiceException extends RuntimeException {

    public TranslateServiceException(String message) {
        super(message);
    }

    public TranslateServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

