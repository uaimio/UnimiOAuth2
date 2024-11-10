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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.log4j.Log4j2;

@Log4j2
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

    @GetMapping("/user-by-auth")
    public Object userByToken() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping("/user-role")
    public Object getUserRole(@RequestHeader(value = HttpHeaders.COOKIE) String cookie,
        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
        headers.set(HttpHeaders.SET_COOKIE, cookie);

        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange("http://auth-server:9000/roles/user-role",
                    HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        } catch (RestClientException e) {
            log.error("Errore chiamata authorization server per l'ottenimento del ruolo del chiamante", e);
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

    @GetMapping("/all-roles")
    public Object getAllRoles(@RequestHeader(value = HttpHeaders.COOKIE) String cookie,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
            @AuthenticationPrincipal OAuth2User oauth2User) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authorizedClient.getAccessToken().getTokenValue());
        headers.set(HttpHeaders.SET_COOKIE, cookie);

        ResponseEntity<Object> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange("http://auth-server:9000/roles/find-all",
                    HttpMethod.GET, new HttpEntity<>(headers), Object.class);
        } catch (RestClientException e) {
            log.error("Errore chiamata authorization server per l'ottenimento del ruolo del chiamante", e);
        }

        return responseEntity != null ? responseEntity.getBody() : null;
    }

    @GetMapping("/resource-base")
    public String helloWorld(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user)
            throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                baseResourceServerUri, HttpMethod.GET,
                request, String.class);

        return "Response from Resource Server: " + response.getBody();
    }
}
