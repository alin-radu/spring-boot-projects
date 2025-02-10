package com.dev.task_tracker_app.services;

import com.dev.task_tracker_app.domain.entities.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    List<Task> getTasksByTaskListId(UUID taskListId);
    Task createTask(UUID taskListId, Task task);
    Optional<Task> getTaskById(UUID taskListId, UUID taskId);
    Task updateTask(UUID taskListId, UUID taskId, Task task);
    void deleteByTaskListIdAndId(UUID taskListId, UUID taskId);
}