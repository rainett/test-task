package io.rainett.testtask.repository;

import io.rainett.testtask.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findByBirthdayBetween(LocalDate birthdayStart, LocalDate birthdayEnd, Pageable pageable);
}
