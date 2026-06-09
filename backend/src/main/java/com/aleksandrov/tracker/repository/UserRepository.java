package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.User;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByEmailAndWorkspaceId(String email, Integer workspaceId);
    Optional<User> findByEmailAndPasswordHash(String email, String passwordHash);
    List<User> findByWorkspaceId(Integer workspaceId);
}