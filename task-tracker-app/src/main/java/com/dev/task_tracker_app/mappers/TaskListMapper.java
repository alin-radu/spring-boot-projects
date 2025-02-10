package com.dev.task_tracker_app.mappers;

import com.dev.task_tracker_app.domain.dto.TaskListDto;
import com.dev.task_tracker_app.domain.entities.TaskList;

public interface TaskListMapper {
    TaskList fromDto(TaskListDto taskListDto);

    TaskListDto toDto(TaskList taskList);
}
