package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.ProjectStatus;

import java.util.Optional;
import java.util.List;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long>{

}