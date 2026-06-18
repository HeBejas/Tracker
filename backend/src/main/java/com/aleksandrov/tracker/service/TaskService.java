package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.dto.CreateTaskDto;
import com.aleksandrov.tracker.dto.TaskParticipantDto;
import com.aleksandrov.tracker.dto.TaskParticipantResponseDto;
import com.aleksandrov.tracker.dto.TaskResponseDto;

import com.aleksandrov.tracker.model.Task;
import com.aleksandrov.tracker.model.TaskParticipant;
import com.aleksandrov.tracker.model.TaskStatus;
import com.aleksandrov.tracker.model.TaskPriority;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.model.Project;

import com.aleksandrov.tracker.repository.TaskRepository;
import com.aleksandrov.tracker.repository.TaskParticipantRepository;
import com.aleksandrov.tracker.repository.TaskStatusRepository;
import com.aleksandrov.tracker.repository.TaskPriorityRepository;
import com.aleksandrov.tracker.repository.UserRepository;
import com.aleksandrov.tracker.repository.ProjectRepository;

import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskParticipantRepository taskParticipantRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskPriorityRepository taskPriorityRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<Map<String, Object>> getTasksByWorkspace(Long workspaceId) {
        List<Task> tasks = taskRepository.findByProject_Workspace_Id(workspaceId);

        return tasks.stream().map(task -> {
            Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("projectId", task.getProject().getId());
            map.put("projectName", task.getProject().getName());
            map.put("statusId", task.getStatus().getId());
            map.put("priorityId", task.getPriority().getId());
            map.put("deadlineDate", task.getDeadlineDate());
            map.put("createdAt", task.getCreatedAt());
            map.put("updatedAt", task.getUpdatedAt());
            return map;
        }).toList();
    }

    public List<TaskResponseDto> getTasksByProject(Long projectId) {
//        return taskRepository.findByProject_Id(projectId);
        List<Task> tasks = taskRepository.findByProject_Id(projectId);
        return tasks.stream().map(task -> TaskResponseDto.builder()
                .id(task.getId())
                .name(task.getName())
                .authorId(task.getAuthor().getId())
                .authorName(task.getAuthor().getFullName())
                .projectName(task.getProject().getName())
                .projectId(task.getProject().getId())
                .statusId(task.getStatus().getId())
                .priorityId(task.getPriority().getId())
                .deadlineDate(task.getDeadlineDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .participants(getTaskParticipants(task.getId()))
                .build()
        ).toList();
    }

    public TaskResponseDto getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));

        return TaskResponseDto.builder()
                .id(task.getId())
                .name(task.getName())
                .authorId(task.getAuthor().getId())
                .authorName(task.getAuthor().getFullName())
                .projectName(task.getProject().getName())
                .projectId(task.getProject().getId())
                .statusId(task.getStatus().getId())
                .priorityId(task.getPriority().getId())
                .deadlineDate(task.getDeadlineDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .participants(getTaskParticipants(task.getId()))
                .build();
    }

    public Task createTask(CreateTaskDto dto, Long authorId) {

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Проект не найден"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Task parent = null;
        if (dto.getParentTaskId() != null) {
            parent = taskRepository.findById(dto.getParentTaskId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка создания подзадачи"));
        }

        TaskPriority taskPriority = taskPriorityRepository.findByName(dto.getPriority())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Приоритет '" + dto.getPriority() + "' не найден"));

        TaskStatus taskStatus = taskStatusRepository.findByName(dto.getStatus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус '" + dto.getStatus() + "' не найден"));

        Task task = new Task();

        task.setName(dto.getName());
        task.setProject(project);
        task.setAuthor(author);
        task.setParentTask(parent);
        task.setPriority(taskPriority);
        task.setStatus(taskStatus);

        return taskRepository.save(task);
    }

    public Task updateTaskFields(Long id, Map<String, Object> updates) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));

        if (updates.containsKey("name")) { task.setName((String) updates.get("name")); }
        if (updates.containsKey("description")) { task.setDescription((String) updates.get("description")); }
        if (updates.containsKey("priorityId")) {
            Long priorityId = Long.valueOf(updates.get("priorityId").toString());
            TaskPriority priority = taskPriorityRepository.findById(priorityId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Приоритет не найден"));
            task.setPriority(priority);
        }
        if (updates.containsKey("statusId")) {
            Long statusId = Long.valueOf(updates.get("statusId").toString());
            TaskStatus status = taskStatusRepository.findById(statusId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус не найден"));
            task.setStatus(status);
        }
        if (updates.containsKey("deadlineDate")) {
            String dateStr = updates.get("deadlineDate").toString();
            OffsetDateTime offsetTime = OffsetDateTime.parse(dateStr);
            OffsetDateTime localTime = offsetTime
                    .atZoneSameInstant(java.time.ZoneId.systemDefault())
                    .toOffsetDateTime();

            task.setDeadlineDate(localTime);
        }

        return taskRepository.save(task);
    }

    public TaskParticipantResponseDto addTaskParticipant(Long taskId, TaskParticipantDto dto) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        TaskParticipant participant = new TaskParticipant();
        participant.setTask(task);
        participant.setUser(user);
        participant.setRoleId(dto.getRoleId());
        participant.setAssignedAt(OffsetDateTime.now());

        taskParticipantRepository.save(participant);
        return mapParticipant(participant);
    }

    public void removeTaskParticipant(Long taskId, Long userId, Long roleId) {
        taskParticipantRepository.findByTaskAndUserAndRole(taskId, userId, roleId)
                .ifPresent(taskParticipantRepository::delete);
    }

    private List<TaskParticipantResponseDto> getTaskParticipants(Long taskId) {
        return taskParticipantRepository.findByTask_Id(taskId).stream().map(this::mapParticipant).toList();
    }

    private TaskParticipantResponseDto mapParticipant(TaskParticipant participant) {
        TaskParticipantResponseDto dto = new TaskParticipantResponseDto();
        dto.setUserId(participant.getUser().getId());
        dto.setUserName(participant.getUser().getFullName());
        dto.setRoleId(participant.getRoleId());
        dto.setAssignedAt(participant.getAssignedAt());
        return dto;
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Задача с id " + id + " не найдена"));
        taskRepository.delete(task);
    }

    public List<Map<String, Object>> getMyTasksInWorkspace(Long workspaceId, Long userId) {
        List<TaskParticipant> participants = taskParticipantRepository.findByUserIdAndWorkspaceId(userId, workspaceId);

        return participants.stream().map(participant -> {
            Task task = participant.getTask();
            Map<String, Object> map = new java.util.HashMap<>();
            
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("projectId", task.getProject().getId());
            map.put("projectName", task.getProject().getName());
            map.put("statusId", task.getStatus().getId());
            map.put("priorityId", task.getPriority().getId());
            map.put("deadlineDate", task.getDeadlineDate());
            map.put("createdAt", task.getCreatedAt());
            map.put("updatedAt", task.getUpdatedAt());
            
            map.put("roleId", participant.getRoleId());
            
            return map;
        }).toList();
    }
}