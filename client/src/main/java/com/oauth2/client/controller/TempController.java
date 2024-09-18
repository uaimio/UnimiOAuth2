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
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PostMapping;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/clienttempo-base")
public class TempController {

    @Value("${resourceserver.uri}${resourceserver.documents-url}")
    private String resourceServerUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static Logger logger = LoggerFactory.getLogger(TempController.class);

    @GetMapping("/hello-world")
    public String getMethodName() {
        return "Hello world";
    }

    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal DefaultOidcUser user) throws JsonProcessingException {
        return Collections.singletonMap("user", user);
    }

    @GetMapping("/hi")
    public Map<String, Object> helloWorld(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user) {
        return Collections.singletonMap("user", user);
    }

    @GetMapping("/hello")
    public String helloWorldExternal(@AuthenticationPrincipal DefaultOidcUser user)
            throws JsonProcessingException {
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getIdToken().getTokenValue());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(request));
        ResponseEntity<String> response = restTemplate.exchange(
                resourceServerUri + "/resource-base/hello-world",
                HttpMethod.GET, request, String.class);

        return "The return from resource is: " + response.getBody();
    }

    @GetMapping("/documentsList")
    public Object getDocumentsList(@AuthenticationPrincipal DefaultOidcUser user)
            throws JsonProcessingException {
        // logger.debug(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(user));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getIdToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                resourceServerUri, HttpMethod.GET, request, Object.class);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), HttpStatus.OK);
    }

    @GetMapping("/document/{documentId}")
    public ResponseEntity<byte[]> getDocumentByDocumentId(
            @AuthenticationPrincipal DefaultOidcUser user,
            @PathVariable String documentId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getIdToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<byte[]> document = restTemplate.exchange(
                resourceServerUri + documentId, HttpMethod.GET, request, byte[].class);

        return new ResponseEntity<>(document.getBody(), document.getHeaders(), HttpStatus.OK);
    }

    @PostMapping("/document")
    public byte[] insertDocument(
            @AuthenticationPrincipal DefaultOidcUser user,
            @RequestPart MultipartFile file) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getIdToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(file, headers);

        ResponseEntity<byte[]> document = restTemplate.exchange(
                resourceServerUri, HttpMethod.POST, request, byte[].class);

        return document.getBody();
    }
}
