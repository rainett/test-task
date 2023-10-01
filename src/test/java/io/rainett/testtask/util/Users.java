package io.rainett.testtask.util;

import io.rainett.testtask.dto.UserDto;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Users {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
    private static final String EMAIL_POSTFIX = "@gmail.com";
    private static long id = 0;

    public static UserDto generateRandomUser() {
        String email = getRandomIntString();
        LocalDate birthday = LocalDate.of(
                RANDOM.nextInt(1970, 2004),
                RANDOM.nextInt(1, 13),
                RANDOM.nextInt(1, 28)
        );
        return UserDto.builder()
                .id(id++)
                .email(email + EMAIL_POSTFIX)
                .firstName("John")
                .lastName("Random")
                .birthday(birthday)
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
        return RANDOM.ints(10, 0, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining());
    }


}