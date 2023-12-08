package com.accommodation.pricing.analysis.model;

public class SearchData {
	private String keyword;
	private String fromDate;
	private String toDate;
	
	public SearchData() {
   // TODO document why this constructor is empty
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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
	
	
}
