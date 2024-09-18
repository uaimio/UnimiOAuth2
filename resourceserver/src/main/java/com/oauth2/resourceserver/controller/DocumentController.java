package com.oauth2.resourceserver.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.model.dto.ElementDTO;
import com.oauth2.resourceserver.service.PdfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/documents")
public class DocumentController {

    @Autowired
    private PdfService pdfService;

    @GetMapping("/findAll")
    public List<ElementDTO> getAllDocuments() {
        return pdfService.getMetadataAllDocuments().stream().map(ElementDTO::new).toList();
    }

    @PostMapping("/")
    public ResponseEntity<ElementDTO> insertDocument(@RequestPart MultipartFile file) throws IOException {
        Element element = pdfService.save(file);
        if (element != null)
            return new ResponseEntity<>(new ElementDTO(element), HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>((ElementDTO) null, HttpStatus.BAD_REQUEST);
    }
    

    @GetMapping("/{documentId}")
    public ResponseEntity<byte[]> getDocument(@PathVariable String documentId) {
        Element el = pdfService.getDocument(documentId);

        if (el == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + el.getFilename());
        headers.add(HttpHeaders.CONTENT_TYPE, el.getFileType());
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(el.getFileSize()));

        return new ResponseEntity<>(el.getFile(), headers, HttpStatus.OK);
    }

    @PutMapping("/{documentId}")
    public ResponseEntity<ElementDTO> updateDocumentByDocumentId(@PathVariable String documentId, @RequestPart MultipartFile file) throws IOException {
        Element element = pdfService.updateDocument(documentId, file);
        if (element != null)
            return new ResponseEntity<>(new ElementDTO(element), HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>((ElementDTO) null, HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<String> deleteDocument(@PathVariable String documentId) {
        String deletedDocumentId = pdfService.delete(documentId);
        if (deletedDocumentId != null)
            return new ResponseEntity<>(deletedDocumentId, HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>(deletedDocumentId, HttpStatus.BAD_REQUEST);
    }

}
