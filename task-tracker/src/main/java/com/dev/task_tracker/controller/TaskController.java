package com.dev.task_tracker.controller;

import com.dev.task_tracker.domain.dto.TaskDto;
import com.dev.task_tracker.domain.entities.Task;
import com.dev.task_tracker.mapper.TaskMapper;
import com.dev.task_tracker.repositorie.TaskListRepository;
import com.dev.task_tracker.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task-lists/{taskListId}/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskListRepository taskListRepository;
    public TaskController(TaskService taskService, TaskMapper taskMapper, TaskListRepository taskListRepository) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
        this.taskListRepository = taskListRepository;
    }

    @PostMapping
    TaskDto createTask(@PathVariable("taskListId") UUID taskListId, @RequestBody TaskDto taskDto) {
        Task taskCreated = taskService.createTask(taskListId, taskMapper.fromDto(taskDto));

        return taskMapper.toDto(taskCreated);
    }

    @GetMapping
    List<TaskDto> getTasksByTaskListId(@PathVariable("taskListId") UUID taskListId) {
        return taskService.getTasksByTaskListId(taskListId)
                .stream()
                .map(taskMapper::toDto)
                .toList();
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getTaskById(
            @PathVariable("taskListId") UUID taskListId,
            @PathVariable("taskId") UUID taskId
    ) {
        if (!taskListRepository.existsById(taskListId)) {
            throw new IllegalArgumentException("Task List not found with the id: " + taskListId);
        }

        // v1
//        if (taskService.getTaskById(taskId).isPresent()) {
//            TaskDto taskDto = taskMapper.toDto(taskService.getTaskById(taskId).get());
//
//            return ResponseEntity.ok(taskDto);
//        } else {
//
//            return ResponseEntity.notFound().build();
//        }

        // v2
        return taskService.getTaskById(taskId)
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
