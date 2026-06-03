package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.UserRole;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}

