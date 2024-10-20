package com.oauth2.authorizationserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth-server-base")
public class AuthServerBaseController {

    @GetMapping(value = "/hello")
    public String helloWorld() {
        return "Authorized!";
    }
}
