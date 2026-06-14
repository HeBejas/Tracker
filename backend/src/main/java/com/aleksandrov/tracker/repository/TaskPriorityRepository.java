package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.TaskPriority;

import java.util.Optional;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long>{
    Optional<TaskPriority> findByName(String name);
}