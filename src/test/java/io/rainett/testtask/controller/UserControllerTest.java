package io.rainett.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.rainett.testtask.dto.UserBirthdaySearchDto;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.dto.UserDtoPatch;
import io.rainett.testtask.exception.InvalidDateRangeException;
import io.rainett.testtask.service.user.UserService;
import io.rainett.testtask.util.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    @DisplayName("Gets all users")
    public void testGetAllUsers() throws Exception {
        Page<UserDto> usersPage = new PageImpl<>(Users.generateRandomUsersList(10));
        when(userService.getAllUsers(any(Pageable.class)))
                .thenReturn(usersPage);
        testUsersPage(usersPage, this.mockMvc.perform(get("/api/v1/users")));
    }

    @Test
    @DisplayName("Gets users between birthday dates")
    public void testGetUsersByBirthday() throws Exception {
        Page<UserDto> usersPage = new PageImpl<>(Users.generateRandomUsersList(10));
        when(userService.getUsersByBirthday(any(UserBirthdaySearchDto.class), any(Pageable.class)))
                .thenReturn(usersPage);
        UserBirthdaySearchDto searchDto = new UserBirthdaySearchDto(LocalDate.EPOCH, LocalDate.now());
        testUsersPage(usersPage, this.mockMvc.perform(get("/api/v1/users/birthday")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsBytes(searchDto))));
    }

    @Test
    @DisplayName("Gets user by id")
    public void testGetUserById() throws Exception {
        UserDto userDto = Users.generateRandomUser();
        when(userService.getUserById(any(Long.class)))
                .thenReturn(userDto);
        testSingleUser(status().isOk(), userDto, this.mockMvc.perform(get("/api/v1/users/" + userDto.getId())));
    }

    @Test
    @DisplayName("Creates user")
    public void testCreateUser() throws Exception {
        UserDto userDto = Users.generateRandomUser();
        userDto.setPhoneNumber("+38-(067)-333-22-22");
        when(userService.createUser(any(UserDto.class)))
                .thenReturn(userDto);
        testSingleUser(status().isCreated(), userDto, this.mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsBytes(userDto))));
    }

    @Test
    @DisplayName("Updates user")
    public void testUpdateUser() throws Exception {
        UserDto userDto = Users.generateRandomUser();
        when(userService.updateUser(any(Long.class), any(UserDto.class)))
                .thenReturn(userDto);
        testSingleUser(status().isOk(), userDto, this.mockMvc.perform(put("/api/v1/users/" + userDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsBytes(userDto))));
    }

    @Test
    @DisplayName("Updates user partially")
    public void testPartiallyUser() throws Exception {
        UserDtoPatch userPartialDto = new UserDtoPatch();
        UserDto userDto = Users.generateRandomUser();

        when(userService.partiallyUpdateUser(any(Long.class), any(UserDtoPatch.class)))
                .thenReturn(userDto);
        testSingleUser(status().isOk(), userDto, this.mockMvc.perform(patch("/api/v1/users/" + userDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(MAPPER.writeValueAsBytes(userPartialDto))));
    }

    @Test
    @DisplayName("Deletes user")
    public void testDeleteUser() throws Exception {
        doNothing().when(userService).deleteUser(any(Long.class));
        this.mockMvc.perform(delete("/api/v1/users/3")).andDo(print())
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Validates user correctly")
    public void testUserDtoValidation() throws Exception {
        UserDto userDto = new UserDto();
        when(userService.createUser(any(UserDto.class)))
                .thenReturn(userDto);
        testUserDtoWithoutRequiredFields(userDto);
        testUserDtoWithInvalidFieldsValues(userDto);
        testUserBirthdayDtoThrowsWhenRangeIsMissed();
    }

    private void testUserBirthdayDtoThrowsWhenRangeIsMissed() {
        LocalDate from = LocalDate.EPOCH;
        LocalDate to = LocalDate.now();
        assertThrows(InvalidDateRangeException.class, () -> new UserBirthdaySearchDto(to, from));
        assertDoesNotThrow(() -> new UserBirthdaySearchDto(from, to));
    }

    private void testUserDtoWithInvalidFieldsValues(UserDto userDto) throws Exception {
        userDto.setEmail("not_an_email");
        userDto.setPhoneNumber("even_not_a_phone_number");
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors.email").value("Wrong email format"))
                .andExpect(jsonPath("$.fieldErrors.phoneNumber").value("Wrong phone number format"))
                .andReturn();
    }

    private void testUserDtoWithoutRequiredFields(UserDto userDto) throws Exception {
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.fieldErrors.size()").value(4))
                .andExpect(jsonPath("$.fieldErrors.birthday").value("Birth Date is required"))
                .andExpect(jsonPath("$.fieldErrors.lastName").value("Last Name is required"))
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First Name is required"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email is required"))
                .andReturn();
    }

    private static void testUsersPage(Page<UserDto> usersPage, ResultActions resultActions) throws Exception {
        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(usersPage.getSize())))
                .andExpect(jsonPath("$.content[0].id").value(usersPage.getContent().get(0).getId()))
                .andExpect(jsonPath("$.content[1].email").value(usersPage.getContent().get(1).getEmail()))
                .andExpect(jsonPath("$.content[2].firstName").value(usersPage.getContent().get(2).getFirstName()))
                .andExpect(jsonPath("$.content[3].lastName").value(usersPage.getContent().get(3).getLastName()))
                .andExpect(jsonPath("$.content[4].birthday").value(usersPage.getContent().get(4).getBirthday().toString()))
                .andReturn();
    }

    private static void testSingleUser(ResultMatcher expectedStatus, UserDto userDto, ResultActions resultActions) throws Exception {
        resultActions
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(userDto.getEmail()))
                .andExpect(jsonPath("$.firstName").value(userDto.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDto.getLastName()))
                .andExpect(jsonPath("$.birthday").value(userDto.getBirthday().toString()))
                .andReturn();
    }

}