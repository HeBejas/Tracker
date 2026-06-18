package com.aleksandrov.tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.aleksandrov.tracker.model.TaskParticipant;
import com.aleksandrov.tracker.model.TaskParticipantId;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskParticipantRepository extends JpaRepository<TaskParticipant, TaskParticipantId> {
    List<TaskParticipant> findByTask_Id(Long taskId);
    void deleteByTask_Id(Long taskId);
    
    @Query("SELECT tp FROM TaskParticipant tp WHERE tp.task.id = :taskId AND tp.user.id = :userId AND tp.roleId = :roleId")
    Optional<TaskParticipant> findByTaskAndUserAndRole(
        @Param("taskId") Long taskId,
        @Param("userId") Long userId,
        @Param("roleId") Long roleId
    );
    @Query("SELECT p FROM TaskParticipant p WHERE p.user.id = :userId AND p.task.project.workspace.id = :workspaceId")
    List<TaskParticipant> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);
}
