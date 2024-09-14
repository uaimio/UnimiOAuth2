package com.oauth2.resourceserver.service;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Element;

public interface PdfService {

    ObjectId storePdf(MultipartFile file) throws IOException;

    Element save(MultipartFile file) throws IOException;

    Element downloadPdf(ObjectId id) throws IOException;

    List<Element> getMetadataAllDocuments();

    Element getDocument(String documentId);

    Element updateDocument(String documentId, MultipartFile file) throws IOException;

    String delete(String documentId);
}
