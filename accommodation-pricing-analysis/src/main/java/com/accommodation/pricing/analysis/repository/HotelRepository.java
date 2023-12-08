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
	
	@Query(value="{$or:["
			+ "{name:{$regex:?0,$options:'i'}},"
			+ "{location:{$regex:?1,$options:'i'}},"
			+ "{reviewDescription:{$regex:?2,$options:'i'}},"
			+ "{score:{$regex:?3,$options:'i'}},"
			+ "{reviewCount:{$regex:?4,$options:'i'}},"
			+ "{city:{$regex:?5,$options:'i'}},"
			+ "{noOfguests:{$regex:?6,$options:'i'}},"
			+ "{overView:{$regex:?7,$options:'i'}},"
			+ "{amenities:{$regex:?8,$options:'i'}},"
			+ "{allAmenities:{$regex:?9,$options:'i'}},"
			+ "]"
			+ "}")
	public List<Hotel> getHotelsBySearchKeyword(String k1,String k2,String k3,String k4,String k5,String k6,String k7,String k8,String k9,String k10);
	
	@Query(value="{fromDate:{$gte:'?0'},toDate:{$lte:'?1'}}")
	public List<Hotel> getHotelsByDate(String fromDate,String toDate);
	
	

	@Query(value="{$or:["
			+ "{name:{$regex:?0,$options:'i'}},"
			+ "{location:{$regex:?1,$options:'i'}},"
			+ "{reviewDescription:{$regex:?2,$options:'i'}},"
			+ "{score:{$regex:?3,$options:'i'}},"
			+ "{reviewCount:{$regex:?4,$options:'i'}},"
			+ "{city:{$regex:?5,$options:'i'}},"
			+ "{noOfguests:{$regex:?6,$options:'i'}},"
			+ "{overView:{$regex:?7,$options:'i'}},"
			+ "{amenities:{$regex:?8,$options:'i'}},"
			+ "{allAmenities:{$regex:?9,$options:'i'}},"
			+ "],"
			+ "$and:[{fromDate:{$gte:?10}},{toDate:{$lte:?11}}]"
			+ "}")
	public List<Hotel> getHotelByKeywordAndDate(String k1,String k2,String k3,String k4,String k5,String k6,String k7,String k8,String k9,String k10,String fromDate,String toDate);
	
}
