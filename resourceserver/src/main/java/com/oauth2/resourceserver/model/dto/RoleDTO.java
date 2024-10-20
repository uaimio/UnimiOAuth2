package com.oauth2.resourceserver.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    
    private Long idRole;

    private String codiRole;

    private String descRole;
}
