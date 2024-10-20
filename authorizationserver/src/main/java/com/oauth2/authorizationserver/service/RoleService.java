package com.oauth2.authorizationserver.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oauth2.authorizationserver.model.dto.RoleDTO;

@Service
public interface RoleService {

    List<RoleDTO> findAll();

    RoleDTO getCallerRole();
}
