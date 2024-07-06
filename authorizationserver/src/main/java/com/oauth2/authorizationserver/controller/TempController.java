package com.oauth2.authorizationserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth2.authorizationserver.repository.UserRepository;

@RestController
@RequestMapping(value = "/auth-server-base")
public class TempController {

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "Authorized!";
    }
}
