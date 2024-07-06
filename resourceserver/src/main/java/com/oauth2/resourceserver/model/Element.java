package com.oauth2.resourceserver.model;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "documents")
public class Element {
    
    private String documentCode;

    private String documentValue;

    private String filename;

    private String fileType;

    private String fileSize;

    private byte[] file;
}
