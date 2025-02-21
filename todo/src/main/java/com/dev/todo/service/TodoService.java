package com.dev.todo.service;

import com.dev.todo.model.Todo;
import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.List;

public interface TodoService {
    List<Todo> findByUsername(String username);
    Todo addTodo(String username, String description, LocalDate targetDate);
    void deleteTodoById(Integer id);
    Todo findById(Integer id);
    void updateTodo(@Valid Todo todo);
}
