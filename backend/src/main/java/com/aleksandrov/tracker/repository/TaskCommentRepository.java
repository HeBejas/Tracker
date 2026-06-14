package com.aleksandrov.tracker.repository;

import com.aleksandrov.tracker.model.TaskComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {
    List<TaskComment> findByTask_IdOrderByCreatedAtAsc(Long taskId);
}