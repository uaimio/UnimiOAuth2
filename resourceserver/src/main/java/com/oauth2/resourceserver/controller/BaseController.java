package com.oauth2.resourceserver.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/resource-base")
public class BaseController {
    
    @GetMapping
    public String baseController() {
        return "Hello world from Resource Server";
    }
}
