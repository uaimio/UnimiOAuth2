package com.oauth2.resourceserver.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.model.dto.ElementDTO;
import com.oauth2.resourceserver.repository.ElementRepository;
import com.oauth2.resourceserver.service.ElementService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ElementServiceImpl implements ElementService {

    @Autowired
    private ElementRepository elementRepository;

    @Override
    public ResponseEntity<List<ElementDTO>> getMetadataAllDocuments(String codiRole) {
        // si verifica che il documento abbia almeno
        // un ruolo autorizzato tra quelli passati
        List<ElementDTO> documentsList = elementRepository.findAll().stream().filter(element -> {
            return element.getRolesAccess().stream().anyMatch(roles -> roles.contentEquals(codiRole));
        }).map(element -> {
            return new ElementDTO(element);
        }).toList();

        if (CollectionUtils.isEmpty(documentsList))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(documentsList, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ElementDTO> getMetadataDocument(String documentId, String codiRole) {
        Optional<Element> document = getDocumentByDocumentIdAndCodiRole(documentId, codiRole);
        if (document.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(new ElementDTO(document.get()), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getDocumentFile(String documentId, String codiRole) {
        Optional<Element> documentOptional = getDocumentByDocumentIdAndCodiRole(documentId, codiRole);
        if (documentOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + documentOptional.get().getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, documentOptional.get().getFileType());
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(documentOptional.get().getFileSize()));

        return new ResponseEntity<>(documentOptional.get().getFile(), headers, HttpStatus.OK);
    }

    @Transactional
    @Override
    public ResponseEntity<ElementDTO> saveDocument(MultipartFile file, List<String> codiRoleList) throws IOException {
        if (file == null || CollectionUtils.isEmpty(codiRoleList))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Element newDocument = new Element();
        newDocument.setFile(file.getBytes());
        newDocument.setFilename(file.getOriginalFilename());
        newDocument.setFileType(file.getContentType());
        newDocument.setFileSize(file.getSize());
        newDocument.setRolesAccess(codiRoleList);

        newDocument = elementRepository.save(newDocument);
        return new ResponseEntity<>(new ElementDTO(newDocument), HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity<ElementDTO> updateDocument(String documentId, MultipartFile file, String codiRoleUser,
            List<String> codiRoleAccess) throws IOException {

        Optional<Element> documentOptional = getDocumentByDocumentIdAndCodiRole(documentId, codiRoleUser);
        if (documentOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        Element document = documentOptional.get();
        document.setFile(file.getBytes());
        document.setFilename(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setRolesAccess(codiRoleAccess);

        document = elementRepository.save(document);
        return new ResponseEntity<>(new ElementDTO(document), HttpStatus.CREATED);
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteDocument(String documentId, String codiRole) {
        Optional<Element> elementOptional = getDocumentByDocumentIdAndCodiRole(documentId, codiRole);
        if (elementOptional.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        elementRepository.delete(elementOptional.get());
        return new ResponseEntity<>(elementOptional.get().getId(), HttpStatus.ACCEPTED);
    }

    private Optional<Element> getDocumentByDocumentIdAndCodiRole(String documentId, String codiRole) {
        return elementRepository.findById(documentId).filter(el -> el.getRolesAccess().contains(codiRole));
    }

}
