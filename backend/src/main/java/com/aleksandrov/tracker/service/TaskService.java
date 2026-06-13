package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.dto.CreateTaskDto;
import com.aleksandrov.tracker.model.Task;
import com.aleksandrov.tracker.model.TaskStatus;
import com.aleksandrov.tracker.model.TaskPriority;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.model.Project;

import com.aleksandrov.tracker.repository.TaskRepository;
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
    private final TaskStatusRepository taskStatusRepository;
    private final TaskPriorityRepository taskPriorityRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProject_Id(projectId);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Задача не найдена"));
    }

    public Task createTask(CreateTaskDto dto, Long authorId) {

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Проект не найден"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Task parent = taskRepository.findById(dto.getParentTaskId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ошибка создания подзадачи"));

        TaskStatus taskStatus = taskStatusRepository.findByName(dto.getStatus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус '" + dto.getStatus() + "' не найден"));

        Task task = new Task();

        task.setName(dto.getName());
        task.setProject(project);
        task.setAuthor(author);
        task.setParentTask(parent);
        task.setStatus(taskStatus);

        return taskRepository.save(task);
    }

    public Task updateTaskFields(Long id, Map<String, Object> updates) {
        Task task = getTaskById(id);

        if (updates.containsKey("name")) { task.setName((String) updates.get("name")); }
        if (updates.containsKey("description")) { task.setDescription((String) updates.get("description")); }
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

}