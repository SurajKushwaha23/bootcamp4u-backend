package com.bootcamp4u.security;

public class JwtAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JwtAuthenticationException(String message) {
        super(message);
    }

    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }


}
