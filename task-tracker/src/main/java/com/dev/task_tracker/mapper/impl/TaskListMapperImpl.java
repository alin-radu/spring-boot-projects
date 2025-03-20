package com.dev.task_tracker.mapper.impl;

import com.dev.task_tracker.domain.dto.TaskDto;
import com.dev.task_tracker.domain.dto.TaskListDto;
import com.dev.task_tracker.domain.entities.Task;
import com.dev.task_tracker.domain.entities.TaskList;
import com.dev.task_tracker.domain.entities.TaskStatus;
import com.dev.task_tracker.mapper.TaskListMapper;
import com.dev.task_tracker.mapper.TaskMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class TaskListMapperImpl implements TaskListMapper {
    private final TaskMapper taskMapper;

    public TaskListMapperImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskList fromDto(TaskListDto taskListDto) {

        List<Task> taskList = mapList(taskListDto.tasks(), taskMapper::fromDto);

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
        Integer count = getCountOfNullableList(taskList.getTasks());
        Double progress = calculateTaskListProgress(taskList.getTasks());

        List<TaskDto> taskListDto = mapList(taskList.getTasks(), taskMapper::toDto);

        return new TaskListDto(
                taskList.getId(),
                taskList.getTitle(),
                taskList.getDescription(),
                count,
                progress,
                taskListDto

        );
    }

    private <T> Integer getCountOfNullableList(List<T> list) {
        return Optional.ofNullable(list)
                .map(List::size)
                .orElse(0);
    }

    private <T, R> List<R> mapList(List<T> sourceList, Function<T, R> mapper) {
        return Optional.ofNullable(sourceList)
                .map(
                        list -> list.stream()
                                .map(mapper)
                                .toList())
                .orElse(Collections.emptyList());
    }

    private Double calculateTaskListProgress(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty()) {
            return 0.0;
        }

        long closedTaskCount = tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.CLOSED)
                .count();

        return (double) closedTaskCount / tasks.size();
    }
}

// Note: getTasksDtoFromTaskEntity, getTasksEntityFromTaskListDto were replaced with generic method mapList;
//private List<TaskDto> getTasksDtoFromTaskEntity(TaskList taskList) {
//    return Optional.ofNullable(taskList.getTasks())
//            .map(tasks ->
//                    tasks
//                            .stream()
//                            .map(taskMapper::toDto)
//                            .toList())
//            .orElse(Collections.emptyList());
//}
//private List<Task> getTasksEntityFromTaskListDto(TaskListDto taskListDto) {
//    return Optional.ofNullable(taskListDto.tasks())
//            .map(tasks ->
//                    tasks
//                            .stream()
//                            .map(taskMapper::fromDto)
//                            .toList())
//            .orElse(Collections.emptyList());
//}