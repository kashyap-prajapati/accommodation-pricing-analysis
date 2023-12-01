package com.accommodation.pricing.analysis.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Model class for URLParams that will generate query string and params
 * @author KASHAYP PRAJAPATI 110126934
 *
 */
public class URLParams {

	private List<String> queryString;
	private Map<String,Object> queryParams;
	
	public URLParams(String...query) {
		this.queryString = Arrays.asList(query);
	}
	public URLParams(List<String> queryString) {
		this.queryString = queryString;
	}
	public URLParams(Map<String,Object> queryMap) {
		this.queryParams = queryMap;
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
	
	/**
     * Generate a simple query string from the list of query components.
     * @return The generated query string.
     */
	public String generateQueryString() {
		String urlQuery = "";
		for(String str : queryString) {
			urlQuery = urlQuery + "/" + str;
		}
		return urlQuery;
	}
	
	/**
     * Generate a query string and append query parameters.
     * @return The generated query string with appended query parameters.
     */
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
	
	/**
     * Generate query parameters.
     * @return The generated query parameters.
     */
	public String generateQueryParams() {
		String urlQuery= "?";
		for(String key:queryParams.keySet()) {
			urlQuery = urlQuery + key +"="+queryParams.get(key)+"&";
		}
		return urlQuery;
		
	}
	
	
}
