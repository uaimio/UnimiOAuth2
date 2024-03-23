package com.oauth2.resourceserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/resource-base")
public class TempController {
    
    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello world";
    }
}
