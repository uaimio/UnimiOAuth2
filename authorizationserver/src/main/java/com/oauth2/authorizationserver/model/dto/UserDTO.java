package com.oauth2.authorizationserver.model.dto;

import com.oauth2.authorizationserver.model.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {

    private Long idUser;
    
    private String username;

    private String email;

    private RoleDTO role;

    public UserDTO(User user) {
        this.idUser = user.getIdUser();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = new RoleDTO(user.getRole());
    }
}
