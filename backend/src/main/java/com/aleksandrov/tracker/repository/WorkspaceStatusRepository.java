package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.WorkspaceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WorkspaceStatusRepository extends JpaRepository<WorkspaceStatus, Long> {
}

