package io.rainett.testtask.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Wrong email format")
    private String email;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotNull(message = "Birth Date is required")
    @Past(message = "Birth Date should be in the past")
    private LocalDate birthday;
    private String address; //  as far as there is no functionality, it can be a string

    @Pattern(regexp = "\\+\\d{1,3}-\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}", message = "Invalid phone number format")
    private String phoneNumber;

}
