package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.model.Project;
import com.aleksandrov.tracker.model.ProjectStatus;
import com.aleksandrov.tracker.repository.ProjectRepository;
import com.aleksandrov.tracker.repository.ProjectStatusRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor

public class ProjectService {

    private final ProjectRepository projectRepository;
    private final ProjectStatusRepository projectStatusRepository;

    public List<Project> getProjectsByWorkspaceId(Long workspaceId) {
        return projectRepository.findByWorkspace_Id(workspaceId);
    }


}