package com.dev.task_tracker.service;

import com.dev.task_tracker.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Task createTask(UUID taskListId, Task task);
    List<Task> getTasksByTaskListId(UUID taskListId);
    Optional<Task> getTaskById(UUID taskId);
    Optional<Task> getTaskByTaskListIdAndId(UUID taskListId, UUID taskId);
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    void deleteByTaskListIdAndId(UUID taskListId, UUID taskId);
}