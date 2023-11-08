package com.accommodation.pricing.analysis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "hotel-collections")
public class Hotel {

	@Id
	private String id;
	private String price;
	private String name;
	private String description;
	
	public Hotel(String id, String price, String name, String description) {
		super();
		this.id = id;
		this.price = price;
		this.name = name;
		this.description = description;
	}
	
	
}
