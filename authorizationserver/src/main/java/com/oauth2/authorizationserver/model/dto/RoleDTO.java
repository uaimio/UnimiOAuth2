package com.oauth2.authorizationserver.model.dto;

import com.oauth2.authorizationserver.model.Role;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleDTO {
    
    private Long idRole;

    private String codiRole;

    private String descRole;

    public RoleDTO(Role role) {
        this.idRole = role.getIdRole();
        this.codiRole = role.getCodiRole();
        this.descRole = role.getDescRole();
    }
}
