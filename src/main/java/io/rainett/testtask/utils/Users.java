package io.rainett.testtask.utils;

import io.rainett.testtask.dto.UserDto;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Users {

    private static final String EMAIL_POSTFIX = "@gmail.com";

    public static UserDto generateRandomUser() {
        String email = getRandomIntString();
        return UserDto.builder()
                .email(email + EMAIL_POSTFIX)
                .firstName("John")
                .lastName("Random")
                .birthday(LocalDate.EPOCH)
                .build();
    }

    public static List<UserDto> generateRandomUsersList(int size) {
        List<UserDto> users = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            users.add(generateRandomUser());
        }
        return users;
    }

    private static String getRandomIntString() {
        return ThreadLocalRandom.current()
                .ints(10, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }


}
