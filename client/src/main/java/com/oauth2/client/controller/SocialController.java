package com.oauth2.client.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/base")
public class SocialController {

    private ObjectMapper objectMapper; //= new ObjectMapper(new JavaTimeModule());

    private static Logger logger = LoggerFactory.getLogger(SocialController.class);

    SocialController() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

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
    public String helloWorldExternal(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user) throws JsonProcessingException {
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        ResponseEntity<String> response =
                restTemplate.exchange(
                    "http://localhost:8081/resource-base/hello-world",
                    HttpMethod.GET, request, String.class);

        return "The return from resource is: " + response.getBody();
    }
}
