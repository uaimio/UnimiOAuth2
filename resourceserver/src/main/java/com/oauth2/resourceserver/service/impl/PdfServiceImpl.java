package com.oauth2.resourceserver.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.oauth2.resourceserver.model.Element;
import com.oauth2.resourceserver.service.PdfService;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private GridFsTemplate template;

    @Autowired
    private GridFsOperations operations;

    @SuppressWarnings("null")
    @Override
    public ObjectId storePdf(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return template.store(inputStream, file.getOriginalFilename());
        }
    }

    @Override
    public Element downloadPdf(ObjectId id) throws IOException {
        GridFSFile gridFSFile = template.findOne(new Query(Criteria.where("_id").is(id)) );

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
}
