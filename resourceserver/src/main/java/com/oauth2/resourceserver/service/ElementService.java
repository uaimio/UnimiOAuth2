package com.oauth2.resourceserver.service;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.dto.ElementDTO;

public interface ElementService {

    ResponseEntity<List<ElementDTO>> getMetadataAllDocuments(String codiRole);

    ResponseEntity<ElementDTO> getMetadataDocument(String documentId, String codiRole);

    ResponseEntity<byte[]> getDocumentFile(String documentId, String codiRole);

    ResponseEntity<ElementDTO> saveDocument(MultipartFile file, List<String> codiRoleList) throws IOException;

    ResponseEntity<ElementDTO> updateDocument(String documentId, MultipartFile file, String codiRoleUser,
            List<String> codiRoleAccess) throws IOException;

    ResponseEntity<String> deleteDocument(String documentId, String codiRole);
}
