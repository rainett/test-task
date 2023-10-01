package io.rainett.testtask.service.constraint;

import io.rainett.testtask.dto.UserDto;

import java.time.LocalDate;

public interface ConstraintService {
    void checkConstraints(UserDto userDto);
    void checkAgeConstraint(LocalDate birthday);
}
