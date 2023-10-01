package io.rainett.testtask.exception;

import org.springframework.http.HttpStatus;

public class AppException extends RuntimeException {
    public final HttpStatus code;

    public AppException(String message, HttpStatus code) {
        super(message);
        this.code = code;
    }

}
