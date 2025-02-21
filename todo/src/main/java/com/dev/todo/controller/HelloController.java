package com.dev.todo.controller;

import com.dev.todo.model.HelloModel;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(path = "/basic-auth")
    public String basicAuthCheck() {
        return "Success";
    }

    @GetMapping(path = "/hello", produces = "text/plain")
    public String hello(@RequestHeader HttpHeaders headers) {

        System.out.println("---> request Headers: " + headers);

        return "Hello World v1";
    }

    @GetMapping(path = "/hello-bean")
    public HelloModel helloBean() {
        return new HelloModel("Hello World Bean v2");
    }

    @GetMapping(path = "/hello/path-variable/{name}")
    public HelloModel helloPathVariable(@PathVariable String name) {
        return new HelloModel(String.format("Hello World, %s", name));
    }
}
