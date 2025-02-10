package com.dev.task_tracker_app.mappers.impl;

import com.dev.task_tracker_app.domain.dto.TaskDto;
import com.dev.task_tracker_app.domain.dto.TaskListDto;
import com.dev.task_tracker_app.domain.entities.Task;
import com.dev.task_tracker_app.domain.entities.TaskList;
import com.dev.task_tracker_app.domain.entities.TaskStatus;
import com.dev.task_tracker_app.mappers.TaskListMapper;
import com.dev.task_tracker_app.mappers.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class TaskListMapperImpl implements TaskListMapper {
    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {
        List<Task> taskList = Optional.ofNullable(taskListDto.tasks())
                .map(tasks ->
                        tasks
                                .stream()
                                .map(taskMapper::fromDto)
                                .toList())
                .orElse(Collections.emptyList());

        return new TaskList(
                taskListDto.id(),
                taskListDto.title(),
                taskListDto.description(),
                taskList,
                null,
                null
        );
    }

    @Override
    public TaskListDto toDto(TaskList taskList) {
        Integer count = Optional.ofNullable(taskList.getTasks())
                .map(List::size)
                .orElse(0);

        Double progress = calculateTaskListProgress(taskList.getTasks());

        List<TaskDto> taskListDto = Optional.ofNullable(taskList.getTasks())
                .map(tasks ->
                        tasks
                                .stream()
                                .map(taskMapper::toDto)
                                .toList())
                .orElse(Collections.emptyList());

        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                count,
                progress,
                taskListDto

        );
    }

    private Double calculateTaskListProgress(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 0.0;
        }

        long closedTaskCount = tasks
                .stream()
                .filter(task -> task.getStatus() == TaskStatus.CLOSED)
                .count();

        return (double) closedTaskCount / tasks.size();
    }
}
