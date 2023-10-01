package io.rainett.testtask.dto;

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
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String address;
    private String phoneNumber;

}
