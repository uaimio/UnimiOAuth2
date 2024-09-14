package com.oauth2.resourceserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "documents_new")
public class Element {
    
    @Id
    private String id;

    private String documentValue;

    private String filename;

    private String fileType;

    private Long fileSize;

    private byte[] file;
}
