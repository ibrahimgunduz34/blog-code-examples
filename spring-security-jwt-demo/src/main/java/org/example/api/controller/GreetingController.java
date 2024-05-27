package org.example.api.controller;

import org.example.api.dto.GreetingModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    @GetMapping("/greeting")
    @ResponseStatus(HttpStatus.OK)
    public GreetingModel sayHello() {
        return new GreetingModel("Hello World!");
    }
}
