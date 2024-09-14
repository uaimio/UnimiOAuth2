package com.oauth2.resourceserver.model.dto;

import com.oauth2.resourceserver.model.Element;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElementDTO {

    public ElementDTO(Element element) {
        this.id = element.getId();
        this.documentValue = element.getDocumentValue();
        this.filename = element.getFilename();
        this.fileType = element.getFileType();
        this.fileSize = element.getFileSize();
    }
    
    private String id;

    private String documentValue;

    private String filename;

    private String fileType;

    private Long fileSize;
}
