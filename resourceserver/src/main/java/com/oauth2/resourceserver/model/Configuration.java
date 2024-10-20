package com.oauth2.resourceserver.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Document(collection = "configurations")
public class Configuration {
    
    @Id
    private String id;

    private String codiRole;

    private List<String> rolesAccess;
}
