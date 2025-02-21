package com.dev.todo.controller;

import com.dev.todo.model.Todo;
import com.dev.todo.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TodoController {

    private final TodoService todoService;

    @Autowired
    public TodoController(@Qualifier("todoServiceImplJpa") TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/users/{username}/todos")
    public List<Todo> getAllTodosByUsername(@PathVariable String username) {
        return todoService.findByUsername(username);
    }

    @GetMapping("/users/{username}/todos/{id}")
    public Todo retrieveTodo(@PathVariable String username,
                             @PathVariable int id) {
        return todoService.findById(id);
    }

    @DeleteMapping("/users/{username}/todos/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable String username,
                                           @PathVariable int id) {
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/users/{username}/todos/{id}")
    public Todo updateTodo(@PathVariable String username,
                           @PathVariable int id, @RequestBody Todo todo) {
        todoService.updateTodo(todo);
        return todo;
    }

    @PostMapping("/users/{username}/todos")
    public Todo createTodo(@PathVariable String username,
                           @RequestBody Todo todo) {

        return todoService.addTodo(username, todo.getDescription(), todo.getTargetDate());
    }

}
