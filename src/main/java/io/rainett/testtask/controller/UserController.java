package io.rainett.testtask.controller;

import io.rainett.testtask.dto.UserBirthdaySearchDto;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.dto.UserDtoPatch;
import io.rainett.testtask.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        Page<UserDto> usersPage = userService.getAllUsers(pageable);
        return ResponseEntity.ok(usersPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto userDto = userService.getUserById(id);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/birthday")
    public ResponseEntity<Page<UserDto>> getUsersByBirthday(@RequestBody UserBirthdaySearchDto userBirthdaySearchDto,
                                                            Pageable pageable) {
        Page<UserDto> usersPage = userService.getUsersByBirthday(userBirthdaySearchDto, pageable);
        return ResponseEntity.ok(usersPage);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto createdUserDto = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDto userDto) {
        UserDto updatedUserDto = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> partiallyUpdateUser(@PathVariable Long id,
                                                       @Valid @RequestBody UserDtoPatch userDto) {
        UserDto updatedUserDto = userService.partiallyUpdateUser(id, userDto);
        return ResponseEntity.ok(updatedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
