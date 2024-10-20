package com.oauth2.resourceserver.model.dto;

import java.util.ArrayList;
import java.util.List;

import com.oauth2.resourceserver.model.Element;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ElementDTO {

    private String id;

    private String documentValue;

    private String filename;

    private String fileType;

    private Long fileSize;

    private List<String> rolesAccess;

    public ElementDTO(Element element) {
        this.id = element.getId();
        this.documentValue = element.getDocumentValue();
        this.filename = element.getFilename();
        this.fileType = element.getFileType();
        this.fileSize = element.getFileSize();
        this.rolesAccess = new ArrayList<>(element.getRolesAccess());
    }
}
