package com.oauth2.resourceserver.controller;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.model.dto.ElementDTO;
import com.oauth2.resourceserver.service.PdfService;

@RestController
@RequestMapping(path = "/unsecured")
public class UnsecureController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/upload")
    public ResponseEntity<ElementDTO> insertDocument(@RequestPart MultipartFile file) throws IOException {
        Element element = pdfService.save(file);
        if (element != null)
            return new ResponseEntity<>(new ElementDTO(element), HttpStatus.ACCEPTED);
        else
            return new ResponseEntity<>((ElementDTO) null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            byte[] pdfBytes = pdfService.downloadPdf(objectId).getFile();

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/findAll")
    public List<ElementDTO> getAllDocuments() {
        return pdfService.getMetadataAllDocuments().stream().map(ElementDTO::new).toList();
    }
}
