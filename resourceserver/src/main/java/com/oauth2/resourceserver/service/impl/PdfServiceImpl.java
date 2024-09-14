package com.oauth2.resourceserver.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.repository.DocumentRepository;
import com.oauth2.resourceserver.service.PdfService;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @Autowired
    private DocumentRepository documentRepository;

    @SuppressWarnings("null")
    @Override
    public ObjectId storePdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return template.store(inputStream, file.getOriginalFilename());
        }
    }

    @Override
    public List<Element> getMetadataAllDocuments() {
        return documentRepository.findAll();
    }

    @Transactional
    @Override
    public Element save(MultipartFile file) throws IOException {
        if (file == null)
            return null;
        Element newDocument = new Element();
        newDocument.setFile(file.getBytes());
        newDocument.setFilename(file.getOriginalFilename());
        newDocument.setFileType(file.getContentType());
        newDocument.setFileSize(file.getSize());

        return documentRepository.save(newDocument);
    }

    @Override
    public Element downloadPdf(ObjectId id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)));

        Element loadFile = new Element();
        // loadFile.setFile(gridFSFile.getMetadata().get(loadFile));

        if (gridFSFile != null) { // && gridFSFile.getMetadata() != null) {
            loadFile.setFilename(gridFSFile.getFilename());
            // loadFile.setFileType(gridFSFile.getMetadata().get("_contentType").toString());
            // loadFile.setFileSize(gridFSFile.getMetadata().get("fileSize").toString());
            loadFile.setFile(
                    IOUtils.toByteArray(operations.getResource(gridFSFile).getInputStream()));
        }

        return loadFile;
    }

    @Override
    public Element getDocument(String documentId) {
        Element document = documentRepository.findById(documentId).orElse(null);
        return document;
    }

    @Transactional
    @Override
    public Element updateDocument(String documentId, MultipartFile file) throws IOException {
        Element document = documentRepository.findById(documentId).orElse(null);
        if (document == null)
            return null;

        document.setFile(file.getBytes());
        document.setFilename(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileSize(file.getSize());
        
        return documentRepository.save(document);
    }

    @Transactional
    @Override
    public String delete(String documentId) {
        try {
            documentRepository.deleteById(documentId);
            return documentId;
        } catch (Exception e) {
            return null;
        }
    }
}
