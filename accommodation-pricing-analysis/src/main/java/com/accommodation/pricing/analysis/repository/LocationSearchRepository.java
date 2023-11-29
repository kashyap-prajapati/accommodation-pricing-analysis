package com.accommodation.pricing.analysis.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.accommodation.pricing.analysis.model.LocationSearch;

@Repository
public interface LocationSearchRepository extends MongoRepository<LocationSearch, String>  {
	
	
	public List<LocationSearch> findByCtid(String ctid);
	
	@Query(value="{'cid':?0,'category':?1}")
	public List<LocationSearch> getLocationSearchs(String ctid,String category);
	
	public List<LocationSearch> findByCategory(String category);
}
