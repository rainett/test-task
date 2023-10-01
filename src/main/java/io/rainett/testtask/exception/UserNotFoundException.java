package io.rainett.testtask.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends AppException {
    public UserNotFoundException(Long userId) {
        super("User with id = [" + userId + "] was not found", HttpStatus.NOT_FOUND);
    }
}
