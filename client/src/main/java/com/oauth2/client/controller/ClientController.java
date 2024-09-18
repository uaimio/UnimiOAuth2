package com.oauth2.client.controller;

import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/client-base")
public class ClientController {

    @Value("${resourceserver.uri}${resourceserver.documents-url}")
    private String documentsControllerResourceServerUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper; // = new ObjectMapper(new JavaTimeModule());

    private static Logger logger = LoggerFactory.getLogger(ClientController.class);

    @GetMapping("/hello-world")
    public String getMethodName() {
        return "Hello world";
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getName());
    }

    @GetMapping("/hi")
    public String helloWorld(@AuthenticationPrincipal OAuth2User principal) {
        return "Hello world " + principal.getAttribute("login") + "!";
    }

    @GetMapping("/hello")
    public String helloWorldExternal(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user)
            throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        ResponseEntity<String> response = restTemplate.exchange(
                documentsControllerResourceServerUri + "hello-world",
                HttpMethod.GET, request, String.class);

        return "The return from resource is: " + response.getBody();
    }

    @GetMapping("/documentsList")
    public Object getDocumentsList(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user)
            throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                documentsControllerResourceServerUri + "/findAll",
                HttpMethod.GET, request, Object.class);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), HttpStatus.OK);
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<byte[]> getDocumentByDocumentId(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user,
            @PathVariable String documentId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<byte[]> document = restTemplate.exchange(
                documentsControllerResourceServerUri + "/" + documentId,
                HttpMethod.GET, request, byte[].class);

        return new ResponseEntity<>(document.getBody(), document.getHeaders(), HttpStatus.OK);
    }

}
