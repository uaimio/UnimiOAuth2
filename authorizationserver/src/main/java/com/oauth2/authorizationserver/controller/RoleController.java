package com.oauth2.authorizationserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oauth2.authorizationserver.model.dto.RoleDTO;
import com.oauth2.authorizationserver.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(value = "/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/find-all")
    public List<RoleDTO> findAll() {
        return roleService.findAll();
    }

    @GetMapping("/user-role")
    public RoleDTO getCallerRoleDTO() {
        return roleService.getCallerRole();
    }
}
