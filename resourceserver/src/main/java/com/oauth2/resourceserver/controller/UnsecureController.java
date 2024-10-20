package com.oauth2.resourceserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Configuration;
import com.oauth2.resourceserver.model.dto.ElementDTO;
import com.oauth2.resourceserver.repository.ConfigurationRepository;
import com.oauth2.resourceserver.service.ElementService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(path = "/unsecured/documents")
public class UnsecureController implements BaseController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private ElementService elementService;

    @GetMapping("/find-all")
    public ResponseEntity<List<ElementDTO>> getAllDocuments(@RequestHeader(required = false, name = "Authorization") String token) throws Exception {
        return elementService.getMetadataAllDocuments(getUserRole(token));
    }

    @PostMapping("/")
    public ResponseEntity<ElementDTO> insertDocument(@RequestHeader(required = false, name = "Authorization") String token,
            @RequestPart MultipartFile file, @RequestPart List<String> codiRoleAccess) throws Exception {

        return elementService.saveDocument(file, checkRolesAndSetIfNull(codiRoleAccess, token));
    }

    @GetMapping("/{documentId}/metadata")
    public ResponseEntity<ElementDTO> getMetadataDocument(@RequestHeader(required = false, name =  "Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.getMetadataDocument(documentId, getUserRole(token));
    }
    

    @GetMapping("/{documentId}")
    public ResponseEntity<byte[]> getDocument(@RequestHeader(required = false, name = "Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.getDocumentFile(documentId, getUserRole(token));
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<ElementDTO> updateDocumentByDocumentId(@RequestHeader(required = false, name = "Authorization") String token,
            @PathVariable String documentId, @RequestPart MultipartFile file, @RequestBody List<String> codiRoleAccess) throws Exception {

        return elementService.updateDocument(documentId, file, getUserRole(token), checkRolesAndSetIfNull(codiRoleAccess, token));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@RequestHeader(required = false, name = "Authorization") String token,
            @PathVariable String documentId) throws Exception {

        return elementService.deleteDocument(documentId, getUserRole(token));
    }

    @Override
    public List<String> checkRolesAndSetIfNull(List<String> codiRoleAccess, String token) throws Exception {
        log.info("Token value [atteso null]: {}", token);
        log.info("codiRoleAccess value [atteso null]: {}", codiRoleAccess);

        Configuration conf = configurationRepository.findAll().stream().findFirst().get();
        return conf.getRolesAccess();
    }

    @Override
    public String getUserRole(String token) throws Exception {
        log.info("Token value [atteso null]: {}", token);
        Configuration conf = configurationRepository.findAll().stream().findFirst().get();
        log.info("Codi role ottenuto: {}", conf.getCodiRole());
        return conf.getCodiRole();
    }
}
