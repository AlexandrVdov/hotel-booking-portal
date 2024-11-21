package com.example.repository;

import com.example.entity.Registration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends MongoRepository<Registration, String> {
}
