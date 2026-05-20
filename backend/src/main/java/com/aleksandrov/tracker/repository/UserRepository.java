//Здесь Spring Data JPA сам сгенерирует SQL-запрос.
package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);

}