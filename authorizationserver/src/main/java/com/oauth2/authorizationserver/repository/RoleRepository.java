package com.oauth2.authorizationserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oauth2.authorizationserver.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
