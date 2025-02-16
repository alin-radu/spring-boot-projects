package com.dev.task_tracker.domain.dto;

import com.dev.task_tracker.domain.entities.TaskPriority;
import com.dev.task_tracker.domain.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TaskDto(
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status
) {
}
