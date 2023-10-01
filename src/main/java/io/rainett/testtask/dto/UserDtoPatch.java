package io.rainett.testtask.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UserDtoPatch {

    private Long id;

    @Email(message = "Wrong email format")
    private String email;
    private String firstName;
    private String lastName;

    @Past(message = "Birth Date should be in the past")
    private LocalDate birthday;
    private String address;

    @Pattern(regexp = "\\+\\d{1,3}-\\(\\d{3}\\)-\\d{3}-\\d{2}-\\d{2}", message = "Invalid phone number format")
    private String phoneNumber;

}
