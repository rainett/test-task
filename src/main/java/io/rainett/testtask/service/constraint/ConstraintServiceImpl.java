package io.rainett.testtask.service.constraint;

import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.exception.AgeRestrictionException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class ConstraintServiceImpl implements ConstraintService {

    @Value("${constraint.age}")
    private int AGE;

    @Override
    public void checkConstraints(UserDto userDto) {
        checkAgeConstraint(userDto.getBirthday());
    }

    @Override
    public void checkAgeConstraint(LocalDate birthday) {
        long age = ChronoUnit.YEARS.between(birthday, LocalDate.now());
        if (age < AGE) {
            throw new AgeRestrictionException(age);
        }
    }

}
