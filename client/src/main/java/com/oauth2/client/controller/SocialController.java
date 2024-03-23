package com.oauth2.client.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/base")
public class SocialController {

    @GetMapping("/hello-world")
    public String getMethodName() {
        return "Hello world";
    }
    
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal);//principal.getAttribute("login"));
    }

    @GetMapping("/hi")
    public String helloWorld(@AuthenticationPrincipal OAuth2User principal) {
        return "Hello world " + principal.getAttribute("login") + "!";
    }

    @GetMapping("/hello")
    public String helloWorldExternal(@AuthenticationPrincipal OAuth2User user) {
        RestTemplate restTemplate = new RestTemplate();

        // new HttpEntity<>(Collections.singletonMap("token", user.getAttribute("")));
        ResponseEntity<String> response =
                restTemplate.exchange(
                    "http://localhost:8081/resource-base/hello-world",
                    HttpMethod.GET, null, String.class);

        return "The return from resource is: " + response.getBody();
    }
}
