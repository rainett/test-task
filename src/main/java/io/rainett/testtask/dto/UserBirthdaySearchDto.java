package io.rainett.testtask.dto;

import io.rainett.testtask.exception.InvalidDateRangeException;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
public class UserBirthdaySearchDto {

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;

    public UserBirthdaySearchDto(LocalDate from, LocalDate to) {
        validate(from, to);
        this.from = from;
        this.to = to;
    }

    public void setFrom(LocalDate from) {
        if (this.to != null) {
            validate(from, this.to);
        }
        this.from = from;
    }

    public void setTo(LocalDate to) {
        if (this.from != null) {
            validate(this.from, to);
        }
        this.to = to;
    }

    private static void validate(LocalDate from, LocalDate to) {
        if (from == null || to == null || !from.isBefore(to)) {
            throw new InvalidDateRangeException(from, to);
        }
    }
}
