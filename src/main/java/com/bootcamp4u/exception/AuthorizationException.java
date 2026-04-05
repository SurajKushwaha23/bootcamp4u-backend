package com.bootcamp4u.exception;

public class AuthorizationException extends RuntimeException {
    public AuthorizationException(String message) {
        super(message);
    }
    public AuthorizationException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public AuthorizationException(Throwable throwable) {
        super(throwable);
    }
}
