package com.accommodation.pricing.analysis.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.accommodation.pricing.analysis.validator.Validator;

@Document
public class Hotel {

	@Id
	private String id;
	private String price;
	private String name;
	private String description;
	private String location;
	
	private String url;
	private String reviewDescription;
	private String score;
	private String reviewCount;
	private String noOfguests;
	
	
	private String noOfRooms;
	private String city;
	private String fromDate;
	private String toDate;
	private String overView;
	private String address;
	private List<String> amenities;
	private List<String> allAmenities;

	
	
	public Hotel() {
		
	}
	
	public Hotel(String price, String name, String description, String location, String address, List<String> amenities,
			String city, String fromDate, String toDate, String url, String reviewDescription, String score,
			String reviewCount, String noOfguests, String noOfRooms) {
		super();
		this.price =  Validator.removeSpecialCharacterFromNumber(price);
		this.name =  Validator.removeSpecialCharacterFromText(name);
		this.description = Validator.removeSpecialCharacterFromText(description);
		this.location = Validator.removeSpecialCharacterFromText(location);
		this.address = Validator.removeSpecialCharacterFromText(address);
		this.amenities = amenities;
		this.city = Validator.removeSpecialCharacterFromText(city);
		this.fromDate = fromDate;
		this.toDate = toDate;
		this.url = url;
		this.reviewDescription = Validator.removeSpecialCharacterFromText(reviewDescription);
		this.score = score;
		this.reviewCount = reviewCount;
		this.noOfguests = noOfguests;
		this.noOfRooms = noOfRooms;
	}
	
	



	@Override
	public String toString() {
		return "Hotel [id=" + id + ", price=" + price + ", name=" + name + ", description=" + description
				+ ", location=" + location + ", url=" + url + ", reviewDescription=" + reviewDescription + ", score="
				+ score + ", reviewCount=" + reviewCount + ", noOfguests=" + noOfguests + ", noOfRooms=" + noOfRooms
				+ ", city=" + city + ", fromDate=" + fromDate + ", toDate=" + toDate + ", overView=" + overView
				+ ", address=" + address + ", amenities=" + amenities + ", allAmenities=" + allAmenities + "]";
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = Validator.removeSpecialCharacterFromNumber(price) ;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = Validator.removeSpecialCharacterFromText(name);
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = Validator.removeSpecialCharacterFromText(description);
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = Validator.removeSpecialCharacterFromText(location);
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = Validator.removeSpecialCharacterFromText(address);
	}
	public List<String> getAmenities() {
		return amenities;
	}
	public void setAmenities(List<String> amenities) {
		this.amenities = amenities;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = Validator.removeSpecialCharacterFromText(city);
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getReviewDescription() {
		return reviewDescription;
	}
	public void setReviewDescription(String reviewDescription) {
		this.reviewDescription = Validator.removeSpecialCharacterFromText(reviewDescription);
	}
	public String getScore() {
		return score;
	}
	public void setScore(String score) {
		this.score = score;
	}
	public String getReviewCount() {
		return reviewCount;
	}
	public void setReviewCount(String reviewCount) {
		this.reviewCount = reviewCount;
	}
	public String getNoOfguests() {
		return noOfguests;
	}
	public void setNoOfguests(String noOfguests) {
		this.noOfguests = noOfguests;
	}
	public String getNoOfRooms() {
		return noOfRooms;
	}
	public void setNoOfRooms(String noOfRooms) {
		this.noOfRooms = noOfRooms;
	}

	public List<String> getAllAmenities() {
		return allAmenities;
	}

	public void setAllAmenities(List<String> allAmenities) {
		this.allAmenities = allAmenities;
	}

	public String getOverView() {
		return overView;
	}

	public void setOverView(String overView) {
		this.overView = overView;
	}	
	
	
	
}
