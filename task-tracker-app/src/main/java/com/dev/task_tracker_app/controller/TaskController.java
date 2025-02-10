package com.dev.task_tracker_app.controller;

import com.dev.task_tracker_app.domain.dto.TaskDto;
import com.dev.task_tracker_app.domain.entities.Task;
import com.dev.task_tracker_app.mappers.TaskMapper;
import com.dev.task_tracker_app.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-lists/{taskListId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @GetMapping
    List<TaskDto> getTasksByTaskListId(@PathVariable("taskListId") UUID taskListId) {
        return taskService.getTasksByTaskListId(taskListId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @PostMapping
    TaskDto createTask(@PathVariable("taskListId") UUID taskListId, @RequestBody TaskDto taskDto) {
        Task taskCreated = taskService.createTask(taskListId, taskMapper.fromDto(taskDto));

        return taskMapper.toDto(taskCreated);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId
    ) {
        return taskService.getTaskById(taskListId, taskId)
                .map(taskMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskId}")
    TaskDto updateTask(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId,
            @RequestBody TaskDto taskDto
    ) {
        Task taskUpdated = taskService.updateTask(taskListId, taskId, taskMapper.fromDto(taskDto));

        return taskMapper.toDto(taskUpdated);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId
    ) {
        taskService.deleteByTaskListIdAndId(taskListId, taskId);

        return ResponseEntity.noContent().build();
    }

}
