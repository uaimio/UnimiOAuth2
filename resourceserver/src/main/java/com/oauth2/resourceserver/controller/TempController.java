package com.oauth2.resourceserver.controller;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.service.PdfService;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/resource-base")
public class TempController {

    @Autowired
    private PdfService pdfService;
    
    @GetMapping("/hello-world")
    public String getHelloWorld() {
        return "Hello world";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable String id) {
        try {
            ObjectId objectId = new ObjectId(id);
            Element el = pdfService.downloadPdf(objectId);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");
            headers.add(HttpHeaders.CONTENT_LENGTH, el.getFileSize());

            return new ResponseEntity<>(el.getFile(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
