package com.dev.task_tracker.mappers;

import com.dev.task_tracker.domain.dto.TaskListDto;
import com.dev.task_tracker.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
