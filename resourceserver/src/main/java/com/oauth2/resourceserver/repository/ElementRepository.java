package com.oauth2.resourceserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.oauth2.resourceserver.model.Element;

import java.util.List;
import java.util.Optional;

@Repository
public interface ElementRepository extends MongoRepository<Element, String> {

    @NonNull Optional<Element> findById(@NonNull String id);

    Optional<Element> findByIdAndRolesAccess(String id, List<String> rolesAccess);
}