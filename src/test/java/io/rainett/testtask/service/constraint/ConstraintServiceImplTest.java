package io.rainett.testtask.service.constraint;

import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.exception.AgeRestrictionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ConstraintServiceImplTest {

    private final int AGE = 18;

    @InjectMocks
    private ConstraintServiceImpl constraintService;

    @Test
    @DisplayName("Checks all constraints")
    public void testCheckConstraints() throws NoSuchFieldException, IllegalAccessException {
        setUpConstraints();
        UserDto userDto = new UserDto();
        LocalDate birthday = LocalDate.now().minusYears(AGE);
        userDto.setBirthday(birthday);
        assertDoesNotThrow(() -> constraintService.checkConstraints(userDto));
        userDto.setBirthday(birthday.plusDays(1));
        assertThrows(AgeRestrictionException.class, () -> constraintService.checkConstraints(userDto));
    }

    private void setUpConstraints() throws NoSuchFieldException, IllegalAccessException {
        Field age = constraintService.getClass().getDeclaredField("AGE");
        age.setAccessible(true);
        age.set(constraintService, AGE);
    }

}