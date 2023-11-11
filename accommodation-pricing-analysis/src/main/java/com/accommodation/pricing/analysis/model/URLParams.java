package com.accommodation.pricing.analysis.model;

import java.util.Arrays;
import java.util.List;

public class URLParams {

	private List<String> queryString;
	
	public URLParams(String...query) {
		this.queryString = Arrays.asList(query);
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
	
	
}
