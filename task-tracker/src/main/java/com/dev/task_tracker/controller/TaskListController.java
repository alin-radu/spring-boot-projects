package com.dev.task_tracker.controller;

import com.dev.task_tracker.domain.dto.TaskListDto;
import com.dev.task_tracker.domain.entities.TaskList;
import com.dev.task_tracker.mappers.TaskListMapper;
import com.dev.task_tracker.services.TaskListService;
import com.dev.task_tracker.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/task-lists")
public class TaskListController {

    private final TaskListService taskListService;
    private final TaskListMapper taskListMapper;
    public TaskListController(TaskListService taskListService, TaskListMapper taskListMapper, TaskService taskService) {
        this.taskListService = taskListService;
        this.taskListMapper = taskListMapper;
    }

    @GetMapping
    public List<TaskListDto> getTaskLists() {

        return taskListService.getTaskLists()
                .stream()
                .map(taskListMapper::toDto)
                .toList();
    }

    @PostMapping
    public TaskListDto createTaskList(@RequestBody TaskListDto taskListDto) {
        TaskList createdTask = taskListService.createTaskList(taskListMapper.fromDto(taskListDto));

        return taskListMapper.toDto(createdTask);
    }

    @GetMapping("/{taskListId}")
    public ResponseEntity<TaskListDto> getTaskListById(@PathVariable("taskListId") UUID taskListId) {

        return taskListService.getTaskList(taskListId)
                .map(taskListMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{taskListId}")
    public TaskListDto updateTaskList(
            @PathVariable("taskListId") UUID taskListId,
            @RequestBody TaskListDto taskListDto
    ) {
        TaskList updatedTaskList = taskListService.updateTaskList(
                taskListId, taskListMapper.fromDto(taskListDto)
        );

        return taskListMapper.toDto(updatedTaskList);
    }

    @DeleteMapping("/{taskListId}")
    public ResponseEntity<Void> deleteTaskListById(@PathVariable("taskListId") UUID taskListId) {
        taskListService.deleteTaskList(taskListId);

        return ResponseEntity.noContent().build();
    }
}

