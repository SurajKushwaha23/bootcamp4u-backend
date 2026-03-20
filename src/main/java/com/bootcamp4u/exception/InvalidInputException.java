package com.bootcamp4u.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Which corresponds to the HTTP 400 status code
public class InvalidInputException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message){
        super(message);
    }

    public InvalidInputException(String message, Throwable cause){
        super(message,cause);
    }

    public InvalidInputException(Throwable cause){
        super(cause);
    }
}
