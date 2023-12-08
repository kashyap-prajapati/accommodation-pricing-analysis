package com.accommodation.pricing.analysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accommodation.pricing.analysis.model.SearchData;
import com.accommodation.pricing.analysis.service.ApiService;

@RestController
@RequestMapping("/api/v1/")
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
public class HomeController {
	
	@Autowired private ApiService apiService;
	
	@GetMapping("/health")
	public ResponseEntity<Object> health(){
		return new ResponseEntity<>("App is healthy and working", HttpStatus.OK);
	}
	
	@PostMapping("/search")
	public ResponseEntity<Object> getSearchData(@RequestBody SearchData searchData){
		return new ResponseEntity<>(apiService.getHotelsBySearchKeyword(searchData), HttpStatus.OK);
	}

}
