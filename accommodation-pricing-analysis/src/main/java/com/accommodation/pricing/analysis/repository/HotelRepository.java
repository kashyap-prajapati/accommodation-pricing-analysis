package com.accommodation.pricing.analysis.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.accommodation.pricing.analysis.model.Hotel;

/**
 * This is the class for Hotel Repository
 * @author KASHYAP PRAJAPATI 110126934
 *
 */
@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {

	@Query(value="{city:{$regex:?0,$options:'i'}}")
	public List<Hotel> getHotelByCity(String city);
	
	@Query(value="{name:{$regex:?0,$options:'i'}}")
	public List<Hotel> getHotelByName(String name);
	

}
