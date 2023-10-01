package io.rainett.testtask.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "user_email_uq", columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private LocalDate birthday;
    private String address; //  as far as there is no functionality, it can be a string
    private String phoneNumber;

}
