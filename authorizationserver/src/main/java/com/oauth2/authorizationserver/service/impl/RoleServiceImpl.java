package com.oauth2.authorizationserver.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.oauth2.authorizationserver.model.dto.RoleDTO;
import com.oauth2.authorizationserver.model.dto.UserDTO;
import com.oauth2.authorizationserver.repository.RoleRepository;
import com.oauth2.authorizationserver.service.RoleService;
import com.oauth2.authorizationserver.service.UserService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Override
    public List<RoleDTO> findAll() {
        return roleRepository.findAll().stream().map(RoleDTO::new).collect(Collectors.toList());
    }

    @Override
    public RoleDTO getCallerRole() {
        String usernameCaller = getUsernameCaller();
        UserDTO userDetails = userService.findByUsername(usernameCaller);
        return userDetails.getRole();
    }

    private String getUsernameCaller() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
