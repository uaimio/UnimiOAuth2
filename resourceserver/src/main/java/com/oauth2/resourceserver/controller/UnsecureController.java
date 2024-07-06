package com.oauth2.resourceserver.controller;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.service.PdfService;

@RestController
@RequestMapping(path = "/unsecured")
public class UnsecureController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadPdf(@RequestParam("file") MultipartFile file) {
        try {
            ObjectId id = pdfService.storePdf(file);
            return new ResponseEntity<>(id.toHexString(), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload PDF", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    // @GetMapping("/download")
    // public ResponseEntity<byte[]> downloadPdf() {
    //     // try {
    //         // ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    //         // Document document = new Document();
    //         // PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
    //         // // PdfDocument pdfDoc = new PdfDocument(writer);
            
    //         // document.add(new Paragraph("Hello, this is a PDF document created by iText."));
    //         // document.close();

    //         // byte[] pdfBytes = byteArrayOutputStream.toByteArray();
    //         byte[] pdfBytes = {};
    //         HttpHeaders headers = new HttpHeaders();
    //         headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=document.pdf");
    //         headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

    //         return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    //     // } catch (DocumentException e) {
    //     //     return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    //     // }
    // }
}
