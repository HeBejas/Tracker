package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.aleksandrov.tracker.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndWorkspaceId(String email, Integer workspaceId);
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
}