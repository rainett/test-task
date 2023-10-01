package io.rainett.testtask.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.rainett.testtask.dto.UserDto;
import io.rainett.testtask.exception.AgeRestrictionException;
import io.rainett.testtask.util.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExceptionControllerTest.class)
class ExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserController controller;

    private final static ObjectMapper MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());


    @Test
    @DisplayName("Handles validation exception")
    public void handlesValidationException() throws Exception {
        UserDto userDto = new UserDto();
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

    @Test
    @DisplayName("Handles app exception")
    public void handlesAppException() throws Exception {
        UserDto userDto = Users.generateRandomUser();
        int age = 54;
        when(controller.createUser(userDto)).thenThrow(new AgeRestrictionException(age));
        this.mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(MAPPER.writeValueAsBytes(userDto)))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Received a user with age = [" + age + "]"))
                .andReturn();
    }

    @Test
    @DisplayName("Handles runtime exception")
    public void handlesRuntimeException() throws Exception {
        when(controller.getUserById(any(Long.class))).thenThrow(new RuntimeException("Not funny"));
        this.mockMvc.perform(get("/api/v1/users/3"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string("Not funny"))
                .andReturn();
    }


}