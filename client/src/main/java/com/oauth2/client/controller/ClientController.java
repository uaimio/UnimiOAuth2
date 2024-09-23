package com.oauth2.client.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/client-base")
public class ClientController {

    @Value("${resourceserver.uri}${resourceserver.resource-base-url}")
    private String baseResourceServerUri;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello-world")
    public String getMethodName() {
        return "Hello world";
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getName());
    }

    @GetMapping("/resource-base")
    public String helloWorld(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseResourceServerUri, HttpMethod.GET,
                request, String.class);

        return "Response from Resource Server: " + response.getBody();
    }
}
