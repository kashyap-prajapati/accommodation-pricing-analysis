package com.accommodation.pricing.analysis.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class URLParams {

	private List<String> queryString;
	private Map<String,Object> queryParams;
	
	public URLParams(String...query) {
		this.queryString = Arrays.asList(query);
	}
	public URLParams(List<String> queryString) {
		this.queryString = queryString;
	}
	
	
	public URLParams(Map<String,Object> queryMap, List<String> queryString) {
		this.queryString = queryString;
		this.queryParams = queryMap;
	}

	public List<String> getQueryString() {
		return queryString;
	}

	public void setQueryString(List<String> queryString) {
		this.queryString = queryString;
	}
	
	public String generateQueryString() {
		String urlQuery = "";
		for(String str : queryString) {
			urlQuery = urlQuery + "/" + str;
		}
		return urlQuery;
	}
	
	public String generateQueryStringAndQueryParams() {
		String urlQuery = "";
		for(String str : queryString) {
			urlQuery = urlQuery + "/" + str;
		}
		urlQuery= urlQuery + "?";
		for(String key:queryParams.keySet()) {
			urlQuery = urlQuery + key +"="+queryParams.get(key)+"&";
		}
		return urlQuery;
		
	}
	
	
}
