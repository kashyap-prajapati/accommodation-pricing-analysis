package com.accommodation.pricing.analysis.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.accommodation.pricing.analysis.model.Hotel;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {

}
