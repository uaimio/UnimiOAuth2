package com.oauth2.resourceserver.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.dto.ElementDTO;
import com.oauth2.resourceserver.model.dto.RoleDTO;
import com.oauth2.resourceserver.service.ElementService;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Log4j2
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/documents")
public class ElementController implements BaseController {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String authServerURL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ElementService elementService;

    @GetMapping("/find-all")
    public ResponseEntity<List<ElementDTO>> getAllDocuments(@RequestHeader("Authorization") String token)
            throws Exception {
        return elementService.getMetadataAllDocuments(getUserRole(token));
    }

    @GetMapping("/{documentId}/metadata")
    public ResponseEntity<ElementDTO> getMetadataDocument(@RequestHeader("Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.getMetadataDocument(documentId, getUserRole(token));
    }

    @GetMapping("/{documentId}")
    public ResponseEntity<byte[]> getDocument(@RequestHeader("Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.getDocumentFile(documentId, getUserRole(token));
    }

    @PostMapping("/")
    public ResponseEntity<ElementDTO> insertDocument(@RequestHeader("Authorization") String token,
            @RequestPart MultipartFile file) throws Exception {

        return elementService.saveDocument(file, checkRolesAndSetIfNull(null, token));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<ElementDTO> updateDocumentByDocumentId(@RequestHeader("Authorization") String token,
            @PathVariable String documentId, @RequestPart MultipartFile file, @RequestBody List<String> codiRoleAccess)
            throws Exception {

        return elementService.updateDocument(documentId, file, getUserRole(token),
                checkRolesAndSetIfNull(codiRoleAccess, token));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@RequestHeader("Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.deleteDocument(documentId, getUserRole(token));
    }

    @Override
    public List<String> checkRolesAndSetIfNull(List<String> codiRoleAccess, String token) throws Exception {
        return CollectionUtils.isEmpty(codiRoleAccess) ? Arrays.asList(getUserRole(token)) : codiRoleAccess;
    }

    @SuppressWarnings("null")
    @Override
    public String getUserRole(String token) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        ResponseEntity<RoleDTO> responseEntity = null;
        try {
            responseEntity = restTemplate.exchange(authServerURL + "/roles/user-role",
                    HttpMethod.GET, new HttpEntity<>(headers), RoleDTO.class);
        } catch (RestClientException e) {
            log.error("Errore chiamata authorization server per l'ottenimento del ruolo del chiamante", e);
        }

        if (responseEntity == null ||
                !responseEntity.getStatusCode().equals(HttpStatus.OK) ||
                responseEntity.getBody() == null ||
                !StringUtils.hasText(responseEntity.getBody().getCodiRole()))
            throw new Exception("Ruolo chiamante non ottenuto");

        return responseEntity.getBody().getCodiRole();
    }
}
