package com.example;

import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
    private final BuildProperties buildProperties;

    public AppController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping
    public String index() {
        return String.format("Current version is: %s", buildProperties.getVersion());
    }
}
