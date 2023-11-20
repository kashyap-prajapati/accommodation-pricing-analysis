package com.accommodation.pricing.analysis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.feignclient.ApiFeignClient;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.repository.LocationSearchRepository;

@Service
public class ApiService {

	@Autowired private LocationSearchRepository locationSearchRepository;
	@Autowired private ApiFeignClient apiFeignClient;
	
	public ApiService() {
		
	}
	
	

	public boolean isLocationSearchAlreadyExists(LocationSearch locationSearch) {
		return locationSearchRepository.findByCtid(locationSearch.getCtid()).isEmpty();
	}
	
	public List<LocationSearch> getSearchSuggestionsFromDb(){
		return locationSearchRepository.findAll();
	}
	
	public List<LocationSearch> getSearch(String search){
		List<LocationSearch> list =  apiFeignClient.getSearchDropdown(search);
		for(LocationSearch locationSearch : list) {
			if(isLocationSearchAlreadyExists(locationSearch)) {
				locationSearch.setSearchQuery(locationSearch.getDisplayname());
				locationSearchRepository.insert(locationSearch);	
			}
		}
		return list;
	}
}
