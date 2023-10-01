package io.rainett.testtask.dto;

import io.rainett.testtask.exception.InvalidDateRangeException;

import java.time.LocalDate;

public record UserBirthdaySearchDto(LocalDate from, LocalDate to) {

    public UserBirthdaySearchDto {
        validate(from, to);
    }

    private static void validate(LocalDate from, LocalDate to) {
        if (from == null || to == null || !from.isBefore(to)) {
            throw new InvalidDateRangeException(from, to);
        }
    }
}
