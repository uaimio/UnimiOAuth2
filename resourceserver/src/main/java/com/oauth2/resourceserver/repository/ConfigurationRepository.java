package com.oauth2.resourceserver.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.oauth2.resourceserver.model.Configuration;

@Repository
public interface ConfigurationRepository extends MongoRepository<Configuration, String> {
}
