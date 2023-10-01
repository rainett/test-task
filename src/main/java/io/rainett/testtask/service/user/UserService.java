package io.rainett.testtask.service.user;

import io.rainett.testtask.dto.UserBirthdaySearchDto;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.dto.UserDtoPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserDto> getAllUsers(Pageable pageable);

    UserDto getUserById(Long id);

    UserDto createUser(UserDto userDto);

    UserDto updateUser(Long id, UserDto userDto);

    UserDto partiallyUpdateUser(Long id, UserDtoPatch userDto);

    void deleteUser(Long id);

    Page<UserDto> getUsersByBirthday(UserBirthdaySearchDto userBirthdaySearchDto, Pageable pageable);
}
