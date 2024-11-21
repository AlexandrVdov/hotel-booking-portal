package com.example.repository;

import com.example.entity.BookingData;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDataRepository extends MongoRepository<BookingData, String> {
}
