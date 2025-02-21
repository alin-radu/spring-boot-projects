package com.dev.todo.service;

import com.dev.todo.model.Todo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoServiceImplMem implements TodoService {
    private static int todosCount = 100;
    private static final List<Todo> todos = new ArrayList<>();

    static {
        todos.add(new Todo(++todosCount, "dev",
                "Learn AWS", LocalDate.now().plusYears(3), false));
        todos.add(new Todo(++todosCount, "dev",
                "Learn Azure", LocalDate.now().plusYears(2), false));
        todos.add(new Todo(++todosCount, "dev",
                "Learn Java", LocalDate.now().plusYears(1), false));
    }

    public TodoServiceImplMem() {
    }

    @Override
    public List<Todo> findByUsername(String username) {
        return todos
                .stream()
                .filter(todo -> todo.getUsername().equalsIgnoreCase(username))
                .toList();
    }

    @Override
    public Todo addTodo(String username, String description, LocalDate targetDate) {
        Todo todo = new Todo(++todosCount, username, description, targetDate, false);
        todos.add(todo);

        return todo;
    }

    @Override
    public void deleteTodoById(Integer id) {
        todos.removeIf(t -> t.getId() == id);
    }

    @Override
    public Todo findById(Integer id) {
        return todos.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }

    @Override
    public void updateTodo(@Valid Todo todo) {
        deleteTodoById(todo.getId());
        todos.add(todo);
    }
}

