package com.oauth2.resourceserver.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;

public interface BaseController {
    
    @GetMapping("/hello-world")
    default public String baseController() {
        return "Hello world from Resource Server";
    }

    List<String> checkRolesAndSetIfNull(List<String> codiRoleAccess, String token) throws Exception;

    String getUserRole(String token) throws Exception;
}
