package com.oauth2.authorizationserver.service;

import org.springframework.stereotype.Service;

import com.oauth2.authorizationserver.model.dto.UserDTO;

@Service
public interface UserService {

    UserDTO findByUsername(String username);
}
