package io.rainett.testtask.exception;

import org.springframework.http.HttpStatus;

public class AgeRestrictionException extends AppException {
    public AgeRestrictionException(long age) {
        super("Received a user with age = [" + age + "]", HttpStatus.FORBIDDEN);
    }
}
