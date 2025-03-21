package com.dev.task_tracker.service;

import com.dev.task_tracker.domain.entities.TaskList;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListService {
    TaskList createTaskList(TaskList taskList);
    List<TaskList> getTaskLists();
    Optional<TaskList> getTaskListById(UUID id);
    TaskList updateTaskList(UUID taskListId, TaskList taskList);
    void deleteTaskList(UUID taskListId);
}
