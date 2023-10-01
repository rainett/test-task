package io.rainett.testtask.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDate;

public class InvalidDateRangeException extends AppException {
    public InvalidDateRangeException(LocalDate from, LocalDate to) {
        super("Received wrong dates range: from = [" + from + "], to = [" + to + "]", HttpStatus.BAD_REQUEST);
    }
}
