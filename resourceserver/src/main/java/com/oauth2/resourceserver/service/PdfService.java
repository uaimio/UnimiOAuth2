package com.oauth2.resourceserver.service;

import java.io.IOException;

import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

import com.oauth2.resourceserver.model.Element;

public interface PdfService {

    public ObjectId storePdf(MultipartFile file) throws IOException;

    public Element downloadPdf(ObjectId id) throws IOException;
}
