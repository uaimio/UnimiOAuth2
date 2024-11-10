package com.oauth2.client.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/client-documents")
public class DocumentController {

    private static Logger logger = LoggerFactory.getLogger(ClientController.class);

    @Value("${resource-server.uri}${resource-server.documents-url}")
    private String documentsControllerResourceServerUri;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

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

    @GetMapping("/documents-list")
    public Object getDocumentsList(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user)
            throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                documentsControllerResourceServerUri + "/find-all",
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

    @PostMapping("/document/")
    public ResponseEntity<Object> insertDocument(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user,
            @RequestPart List<String> codiRoleAccess,
            @RequestPart MultipartFile file) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> multipartBody = new LinkedMultiValueMap<>();
        multipartBody.add("codiRoleAccess", codiRoleAccess);
        multipartBody.add("file", file.getResource());
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<Object> response = restTemplate.postForEntity(documentsControllerResourceServerUri + "/", request, Object.class);

        return new ResponseEntity<>(response.getBody(), response.getHeaders(), HttpStatus.OK);
    }

    @PutMapping("/document/{documentId}")
    public ResponseEntity<Object> updateDocumentByDocumentId(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user,
            @PathVariable String documentId, @RequestPart MultipartFile file,
            @RequestPart List<String> codiRoleAccess) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());

        MultipartBodyBuilder multipartBodyBuilder = new MultipartBodyBuilder();
        multipartBodyBuilder.part("file", file);
        multipartBodyBuilder.part("codiRoleAccess", codiRoleAccess);

        MultiValueMap<String, HttpEntity<?>> multipartBody = multipartBodyBuilder.build();
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(multipartBody, headers);

        ResponseEntity<Object> document = restTemplate.exchange(
                documentsControllerResourceServerUri + "/" + documentId,
                HttpMethod.PUT, request, Object.class);

        return new ResponseEntity<>(document.getBody(), document.getHeaders(), HttpStatus.OK);
    }

    @DeleteMapping("/document/{documentId}")
    public ResponseEntity<String> deleteDocumentByDocumentId(
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient user,
            @PathVariable String documentId) throws JsonProcessingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(user.getAccessToken().getTokenValue());
        HttpEntity<Object> request = new HttpEntity<>(headers);

        ResponseEntity<String> document = restTemplate.exchange(
                documentsControllerResourceServerUri + "/" + documentId,
                HttpMethod.DELETE, request, String.class);

        return new ResponseEntity<>(document.getBody(), document.getHeaders(), HttpStatus.OK);
    }
}
