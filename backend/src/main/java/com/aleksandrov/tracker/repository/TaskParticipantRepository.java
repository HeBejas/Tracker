package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.TaskParticipant;
import com.aleksandrov.tracker.model.TaskParticipantId;
import java.util.List;

@Repository
public interface TaskParticipantRepository extends JpaRepository<TaskParticipant, TaskParticipantId> {
    List<TaskParticipant> findByTask_Id(Long taskId);
    void deleteByTask_Id(Long taskId);
}
