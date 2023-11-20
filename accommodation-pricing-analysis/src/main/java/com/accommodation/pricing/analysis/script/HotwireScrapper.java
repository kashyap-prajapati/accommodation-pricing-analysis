package com.accommodation.pricing.analysis.script;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.driver.Driver;

@Service
public class HotwireScrapper {
	
	private HashMap<String,Object> queryParams;
	private List<String> queryString;
	
	public HotwireScrapper() {
		this.queryParams = new LinkedHashMap<>();
		this.queryString = new LinkedList<>();
	}
	

	
}
