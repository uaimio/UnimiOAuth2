package com.oauth2.resourceserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.oauth2.resourceserver.model.Element;

public interface DocumentRepository extends MongoRepository<Element, String> {
}