package com.accommodation.pricing.analysis.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.accommodation.pricing.analysis.validator.Validator;

@Document
public class LocationSearch {

	@Id
	private String primary_id;
	private String id;
	private String displayname;
	private String loctype;
	private String cid;
	private String rid;
	private String ctid;
	private String lat;
	private String lng;
	private String cc;
	private String country;
	private String rc;
	private String cityname;
	private String timezone;
	private String utc;
	private String ap;
	private String apicode;
	private String citynameshort;
	private String cityonly;
	private String locationname;
	private String searchQuery;
	
	public String getPrimary_id() {
		return primary_id;
	}
	public void setPrimary_id(String primary_id) {
		this.primary_id = primary_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getLoctype() {
		return loctype;
	}
	public void setLoctype(String loctype) {
		this.loctype = loctype;
	}
	public String getCid() {
		return cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public String getCtid() {
		return ctid;
	}
	public void setCtid(String ctid) {
		this.ctid = ctid;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getRc() {
		return rc;
	}
	public void setRc(String rc) {
		this.rc = rc;
	}
	public String getCityname() {
		return cityname;
	}
	public void setCityname(String cityname) {
		this.cityname = cityname;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}
	public String getUtc() {
		return utc;
	}
	public void setUtc(String utc) {
		this.utc = utc;
	}
	public String getAp() {
		return ap;
	}
	public void setAp(String ap) {
		this.ap = ap;
	}
	public String getApicode() {
		return apicode;
	}
	public void setApicode(String apicode) {
		this.apicode = apicode;
	}
	public String getCitynameshort() {
		return citynameshort;
	}
	public void setCitynameshort(String citynameshort) {
		this.citynameshort = citynameshort;
	}
	public String getCityonly() {
		return cityonly;
	}
	public void setCityonly(String cityonly) {
		this.cityonly = cityonly;
	}
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	public String getSearchQuery() {
		return searchQuery;
	}
	public void setSearchQuery(String searchQuery) {
		this.searchQuery = Validator.removeSpecicalCharacterWithSpaceFromText(searchQuery.toLowerCase());
	}
	
	
}