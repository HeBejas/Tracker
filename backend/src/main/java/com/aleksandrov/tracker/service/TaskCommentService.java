package com.aleksandrov.tracker.service;

import com.aleksandrov.tracker.dto.CreateTaskCommentDto;
import com.aleksandrov.tracker.dto.TaskCommentResponseDto;
import com.aleksandrov.tracker.model.Task;
import com.aleksandrov.tracker.model.TaskComment;
import com.aleksandrov.tracker.model.User;
import com.aleksandrov.tracker.repository.TaskCommentRepository;
import com.aleksandrov.tracker.repository.TaskRepository;
import com.aleksandrov.tracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskCommentService {

    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public List<TaskCommentResponseDto> getCommentsByTaskId(Long taskId) {
        return taskCommentRepository.findByTask_IdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(comment -> {
                    TaskCommentResponseDto dto = new TaskCommentResponseDto();
                    dto.setId(comment.getId());
                    dto.setMessage(comment.getMessage());
                    dto.setCreatedAt(comment.getCreatedAt());
                    dto.setTaskId(comment.getTask().getId());
                    dto.setUserId(comment.getUser().getId());
                    dto.setAuthorName(comment.getUser().getFullName());
                    dto.setReplyId(comment.getReplyId());
                    return dto;
                })
                .toList();
    }

    public TaskComment createComment(CreateTaskCommentDto dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден"));

        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Проект не найден"));

        TaskComment comment = new TaskComment();
        comment.setMessage(dto.getMessage());
        comment.setUser(user);
        comment.setTask(task);

        if (dto.getReplyId() != null) {
            TaskComment parentComment = taskCommentRepository.findById(dto.getReplyId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Родительский комментарий не найден"));

            if (!parentComment.getTask().getId().equals(task.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Родительский комментарий принадлежит другому проекту");
            }
            comment.setReplyTo(parentComment);
        }
        return taskCommentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        if (!taskCommentRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Комментарий не найден");
        }
        taskCommentRepository.deleteById(id);
    }
}