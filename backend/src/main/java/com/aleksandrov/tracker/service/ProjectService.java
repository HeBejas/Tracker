package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.dto.CreateProjectDto;
import com.aleksandrov.tracker.model.Workspace;
import com.aleksandrov.tracker.model.Project;
import com.aleksandrov.tracker.model.ProjectStatus;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.repository.ProjectRepository;
import com.aleksandrov.tracker.repository.ProjectStatusRepository;
import com.aleksandrov.tracker.repository.UserRepository;
import com.aleksandrov.tracker.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStatusRepository projectStatusRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public List<Project> getProjectsByWorkspaceId(Long workspaceId) {
        return projectRepository.findByWorkspace_Id(workspaceId);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Проект не найден"));
    }

    public Project createProject(CreateProjectDto dto, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Рабочая среда не найдена"));

        ProjectStatus projectStatus = projectStatusRepository.findByName(dto.getStatus())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус '" + dto.getStatus() + "' не найден"));

        Project project = new Project();
        project.setName(dto.getName());
        project.setWorkspace(workspace);
        project.setAuthor(author);
        project.setStatus(projectStatus);

        project.setTotalTasksCount(0);
        project.setCompletedTasksCount(0);

        return projectRepository.save(project);
    }

    public Project updateProjectFields(Long id, Map<String, Object> updates) {
        Project project = getProjectById(id);

        if (updates.containsKey("name")) { project.setName((String) updates.get("name")); }
        if (updates.containsKey("description")) { project.setDescription((String) updates.get("description")); }
        if (updates.containsKey("statusId")) {
            Long statusId = Long.valueOf(updates.get("statusId").toString());
            ProjectStatus status = projectStatusRepository.findById(statusId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Статус не найден"));
            project.setStatus(status);
        }
        if (updates.containsKey("deadlineDate")) {
            String dateStr = updates.get("deadlineDate").toString();
            OffsetDateTime offsetTime = OffsetDateTime.parse(dateStr);
            OffsetDateTime localTime = offsetTime
                    .atZoneSameInstant(java.time.ZoneId.systemDefault())
                    .toOffsetDateTime();

            project.setDeadlineDate(localTime);
        }
        return projectRepository.save(project);
    }

}