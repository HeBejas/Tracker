package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.UserStatus;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserStatusRepository  extends JpaRepository<UserStatus, Long> {
}

