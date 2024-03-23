package com.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TempController {

    // @GetMapping(value = "/oauth2/v1/authorize")
    // public String hello(@PathVariable(value = "response_type") String value) {
    //     return "Hello!" + value;
    // }
    
    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "Hello world";
    }
}
