package io.rainett.testtask.service;

import io.rainett.testtask.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
}
