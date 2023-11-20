package com.accommodation.pricing.analysis.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.accommodation.pricing.analysis.model.LocationSearch;

@Repository
public interface LocationSearchRepository extends MongoRepository<LocationSearch, String>  {
	
	
	public List<LocationSearch> findByCtid(String ctid);

}
