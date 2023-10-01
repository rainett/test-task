package io.rainett.testtask.service.user;

import io.rainett.testtask.dto.UserBirthdaySearchDto;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.dto.UserDtoPatch;
import io.rainett.testtask.exception.UserNotFoundException;
import io.rainett.testtask.mapper.UserMapper;
import io.rainett.testtask.model.User;
import io.rainett.testtask.repository.UserRepository;
import io.rainett.testtask.service.constraint.ConstraintService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ConstraintService constraintService;

    @Override
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::userToDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToDto)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public Page<UserDto> getUsersByBirthday(UserBirthdaySearchDto birthdayDto, Pageable pageable) {
        return userRepository.findByBirthdayBetween(birthdayDto.getFrom(), birthdayDto.getTo(), pageable)
                .map(userMapper::userToDto);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        constraintService.checkConstraints(userDto);
        User user = userMapper.dtoToUser(userDto);
        user.setId(null);
        User savedUsed = userRepository.save(user);
        return userMapper.userToDto(savedUsed);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        constraintService.checkConstraints(userDto);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthday(userDto.getBirthday());
        user.setAddress(userDto.getAddress());
        user.setPhoneNumber(userDto.getPhoneNumber());
        User updatedUser = userRepository.save(user);
        return userMapper.userToDto(updatedUser);
    }

    @Override
    public UserDto partiallyUpdateUser(Long id, UserDtoPatch userDto) {
        if (userDto.getBirthday() != null) {
            constraintService.checkAgeConstraint(userDto.getBirthday());
        }
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        try {
            reflectionUserPartialUpdate(user, userDto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        User updatedUser = userRepository.save(user);
        return userMapper.userToDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    private static void reflectionUserPartialUpdate(User user, UserDtoPatch userDto)
            throws IllegalAccessException {
        Field[] dtoFields = userDto.getClass().getDeclaredFields();
        Map<String, Object> userFields = Arrays.stream(user.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, f -> f));
        for (Field f : dtoFields) {
            Object fieldValue = f.get(userDto);
            if (fieldValue == null) {
                continue;
            }
            Field userField = (Field) userFields.get(f.getName());
            userField.setAccessible(true);
            userField.set(user, fieldValue);
        }
    }
}
