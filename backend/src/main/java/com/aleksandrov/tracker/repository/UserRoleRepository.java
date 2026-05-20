package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.entity.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {
}