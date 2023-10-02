package io.rainett.testtask.service.user;

import io.rainett.testtask.dto.UserBirthdaySearchDto;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.dto.UserDtoPatch;
import io.rainett.testtask.exception.UserNotFoundException;
import io.rainett.testtask.mapper.UserMapper;
import io.rainett.testtask.model.User;
import io.rainett.testtask.repository.UserRepository;
import io.rainett.testtask.service.constraint.ConstraintService;
import io.rainett.testtask.util.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private ConstraintService constraintService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Gets all users")
    public void getsAllUsers() {
        int totalElements = 20;
        Page<UserDto> usersDtoPage = new PageImpl<>(Users.generateRandomUsersList(totalElements));
        Page<User> usersPage = usersDtoPage.map(dto -> new User());
        when(userRepository.findAll(any(Pageable.class))).thenReturn(usersPage);
        when(userMapper.userToDto(any(User.class))).thenReturn(new UserDto());
        Page<UserDto> result = userService.getAllUsers(Pageable.unpaged());
        assertEquals(totalElements, result.getTotalElements());
    }

    @Test
    @DisplayName("Gets user by id")
    public void getsUserById() {
        UserDto expected = new UserDto(2L, "mail@mail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        when(userMapper.userToDto(any(User.class))).thenReturn(expected);
        UserDto actual = userService.getUserById(2L);
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Throws UserNotFoundException when user was not found")
    public void throwsUserNotFoundException_WhenUserByIdNotFound() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    @DisplayName("Gets users by birthday")
    public void getsUsersByBirthday() {
        int totalElements = 20;
        Page<UserDto> usersDtoPage = new PageImpl<>(Users.generateRandomUsersList(totalElements));
        Page<User> usersPage = usersDtoPage.map(dto -> new User());
        when(userRepository.findByBirthdayBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class)))
                .thenReturn(usersPage);
        when(userMapper.userToDto(any(User.class))).thenReturn(new UserDto());
        UserBirthdaySearchDto birthdayDto = new UserBirthdaySearchDto(LocalDate.EPOCH, LocalDate.now());
        Page<UserDto> result = userService.getUsersByBirthday(birthdayDto, Pageable.unpaged());
        assertEquals(totalElements, result.getTotalElements());
    }

    @Test
    @DisplayName("Creates user")
    public void createsUser() {
        UserDto expected = new UserDto(2L, "mail@mail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        doNothing().when(constraintService).checkConstraints(any(UserDto.class));
        when(userMapper.dtoToUser(any(UserDto.class))).thenReturn(new User());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.userToDto(any(User.class))).thenReturn(expected);
        UserDto result = userService.createUser(expected);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Updates user")
    public void updatesUser() {
        UserDto expected = new UserDto(2L, "mail@mail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        doNothing().when(constraintService).checkConstraints(any(UserDto.class));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(userMapper.userToDto(any(User.class))).thenReturn(expected);
        UserDto result = userService.updateUser(2L, expected);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("Throws UserNotFoundException when user was not found during update")
    public void throwsUserNotFoundException_WhenUserByIdNotFound_DuringUpdate() {
        UserDto expected = new UserDto(2L, "mail@mail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        doNothing().when(constraintService).checkConstraints(any(UserDto.class));
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.updateUser(2L, expected));
    }

    @Test
    @DisplayName("Partially updates user")
    public void partiallyUpdatesUser() {
        User user = new User(2L, "mail@mail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        User updatedUser = new User(2L, "mail@gmail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        UserDto updatedUserDto = new UserDto(2L, "mail@gmail.com", "John", "Doe", LocalDate.EPOCH, null, null);
        UserDtoPatch userDtoPatch = new UserDtoPatch();
        userDtoPatch.setEmail("mail@gmail.com");
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.userToDto(any(User.class))).thenReturn(updatedUserDto);
        UserDto result = userService.partiallyUpdateUser(2L, userDtoPatch);
        assertEquals(updatedUserDto, result);
    }

    @Test
    @DisplayName("Throws UserNotFoundException when user was not found during partial update")
    public void throwsUserNotFoundException_WhenUserByIdNotFound_DuringPartialUpdate() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.partiallyUpdateUser(2L, new UserDtoPatch()));
    }

    @Test
    @DisplayName("Deletes user")
    public void deletesUser() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
        doNothing().when(userRepository).delete(any(User.class));
        userService.deleteUser(2L);
    }

    @Test
    @DisplayName("Throws UserNotFoundException when user was not found during deletion")
    public void throwsUserNotFoundException_WhenUserByIdNotFound_DuringDeletion() {
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(2L));
    }

}