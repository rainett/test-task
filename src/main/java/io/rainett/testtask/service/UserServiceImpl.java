package io.rainett.testtask.service;

import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.utils.Users;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final List<UserDto> users = Users.generateRandomUsersList(5);

    @Override
    public List<UserDto> getAllUsers() {
        return users;
    }
}
