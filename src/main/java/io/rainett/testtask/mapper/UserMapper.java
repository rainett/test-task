package io.rainett.testtask.mapper;

import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);
    User dtoToUser(UserDto userDto);

}
