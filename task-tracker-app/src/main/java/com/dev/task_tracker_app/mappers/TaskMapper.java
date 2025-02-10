package com.dev.task_tracker_app.mappers;

import com.dev.task_tracker_app.domain.dto.TaskDto;
import com.dev.task_tracker_app.domain.entities.Task;

public interface TaskMapper {

    Task fromDto(TaskDto taskDto);

    TaskDto toDto(Task task);
}
