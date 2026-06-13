package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.ProjectComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findByProject_IdOrderByCreatedAtAsc(Long projectId);
}