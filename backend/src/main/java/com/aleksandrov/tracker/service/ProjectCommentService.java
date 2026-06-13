package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.dto.CreateProjectCommentDto;
import com.aleksandrov.tracker.dto.ProjectCommentResponseDto;
import com.aleksandrov.tracker.model.Project;
import com.aleksandrov.tracker.model.ProjectComment;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.repository.ProjectCommentRepository;
import com.aleksandrov.tracker.repository.ProjectRepository;
import com.aleksandrov.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectCommentService {

    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public List<ProjectCommentResponseDto> getCommentsByProjectId(Long projectId) {
        return projectCommentRepository.findByProject_IdOrderByCreatedAtAsc(projectId)
                .stream()
                .map(comment -> {
                    ProjectCommentResponseDto dto = new ProjectCommentResponseDto();
                    dto.setId(comment.getId());
                    dto.setMessage(comment.getMessage());
                    dto.setCreatedAt(comment.getCreatedAt());
                    dto.setProjectId(comment.getProject().getId());
                    dto.setUserId(comment.getUser().getId());
                    dto.setAuthorName(comment.getUser().getFullName());
                    dto.setReplyId(comment.getReplyId());
                    return dto;
                })
                .toList();
    }

    public ProjectComment createComment(CreateProjectCommentDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Project project = projectRepository.findById(dto.getProjectId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Проект не найден"));

        ProjectComment comment = new ProjectComment();
        comment.setMessage(dto.getMessage());
        comment.setUser(user);
        comment.setProject(project);

        if (dto.getReplyId() != null) {
            ProjectComment parentComment = projectCommentRepository.findById(dto.getReplyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Родительский комментарий не найден"));

            if (!parentComment.getProject().getId().equals(project.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Родительский комментарий принадлежит другому проекту");
            }

            comment.setReplyTo(parentComment);
        }

        return projectCommentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        if (!projectCommentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарий не найден");
        }
        projectCommentRepository.deleteById(id);
    }
}