package com.dev.task_tracker.services.impl;

import com.dev.task_tracker.domain.entities.Task;
import com.dev.task_tracker.domain.entities.TaskList;
import com.dev.task_tracker.domain.entities.TaskPriority;
import com.dev.task_tracker.domain.entities.TaskStatus;
import com.dev.task_tracker.exception.ItemNotFoundException;
import com.dev.task_tracker.repositories.TaskListRepository;
import com.dev.task_tracker.repositories.TaskRepository;
import com.dev.task_tracker.services.TaskService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskServiceImpl(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    @Override
    public List<Task> getTasksByTaskListId(UUID taskListId) {
        if (taskListRepository.findById(taskListId).isEmpty()) {
            throw new ItemNotFoundException();
        }
        return taskRepository.findByTaskListId(taskListId);
    }

    @Transactional
    @Override
    public Task createTask(UUID taskListId, Task task) {

        if (task.getId() != null) {
            throw new IllegalArgumentException("Task already has an ID!");
        }

        if (task.getTitle() == null || task.getTitle().isBlank()) {
            throw new IllegalArgumentException("Task must have a title!");
        }

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(ItemNotFoundException::new);

        TaskPriority taskPriority = Optional.ofNullable(task.getPriority()).orElseGet(() -> TaskPriority.MEDIUM);
        TaskStatus taskStatus = TaskStatus.OPEN;

        LocalDateTime now = LocalDateTime.now();

        Task taskToSave = new Task(
                null,
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                taskStatus,
                taskPriority,
                taskList,
                now,
                now
        );

        taskRepository.save(taskToSave);

        return taskToSave;

    }

    @Override
    public Optional<Task> getTaskById(UUID taskListId, UUID taskId) {
        return taskRepository.findByTaskListIdAndId(taskListId, taskId);
    }

    @Transactional
    @Override
    public Task updateTask(UUID taskListId, UUID taskId, Task task) {
        if (task.getId() == null) {
            throw new IllegalArgumentException("Task list must have an ID!");
        }

        if (!Objects.equals(taskId, task.getId())) {
            throw new IllegalArgumentException("Operation not allowed!");
        }

        if (task.getPriority() == null) {
            throw new IllegalArgumentException("Task must have a valid priority!");
        }

        if (task.getStatus() == null) {
            throw new IllegalArgumentException("Task must have a valid status!");
        }

        Task existingTask = getTaskById(taskListId, taskId)
                .orElseThrow(ItemNotFoundException::new);

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());
        existingTask.setUpdatedDate(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    @Override
    public void deleteByTaskListIdAndId(UUID taskListId, UUID taskId) {
        if (getTaskById(taskListId, taskId).isEmpty()) {
            throw new ItemNotFoundException();
        }

        taskRepository.deleteByTaskListIdAndId(taskListId, taskId);
    }
}
