package com.accommodation.pricing.analysis.pageobjects;

public class MomondoPageObject {

	protected static final String NO_OF_PEOPLE_BUTTON_XPATH="//button[@class='Iqt3 Iqt3-mod-stretch Iqt3-mod-bold Button-No-Standard-Style Iqt3-mod-variant-none Iqt3-mod-theme-none Iqt3-mod-shape-rounded-medium Iqt3-mod-shape-mod-default Iqt3-mod-spacing-small Iqt3-mod-size-large']";
	protected static final String NO_OF_PEOPLE_INPUT_XPATH="//div[@class='mH-s-incrementor'][2]/div[@class='bCGf bCGf-mod-theme-default']/input[@class='bCGf-mod-theme-default']";
	protected static final String FROM_INPUT_XPATH="//span[@class='sR_k-date sR_k-mod-hide-arrows sR_k-mod-active']/span[@class='sR_k-value']";
	protected static final String TO_INPUT_XPATH="//span[@class='sR_k-date sR_k-mod-hide-arrows']/span[@class='sR_k-value']";
	
	protected static final String HOTEL_LIST_XPATH ="//div[@class='c8JmD']//div[@class='kzGk kzGk-mod-flexible-height kzGk-mod-responsive']//div[@class='yuAt yuAt-pres-rounded']//div[@class='kzGk-resultInner']";
	protected static final String LAST_PAGE_NUMBER_XPATH="//div[@class='ejnn ejnn-mod-standalone ejnn-mod-responsive']//div[@class='Joiu-buttons']//button[last()-1]";
	protected static final String NEXT_BUTTON_XPATH="//div[@class='ejnn ejnn-mod-standalone ejnn-mod-responsive']//div[@class='Joiu-buttons']//button[last()]";
	protected static final String HOTEL_NAME_LINK_XPATH="//div[@class='c8JmD']/div[2]/div[@class='c71z4']/div/div[@class='kzGk kzGk-mod-flexible-height kzGk-mod-responsive']/div[@class='yuAt yuAt-pres-rounded']/div[@class='kzGk-resultInner']/div[@class='kzGk-middleSection']/div[@class='FLpo FLpo-mod-flexible-height FLpo-mod-responsive']/div[@class='FLpo-info-top']/div[@class='FLpo-hotel-name']/a[@class='FLpo-big-name']";
	protected static final String AMENITIES_XPATH = "//span[@class='tYfO-amenity-name']";
	protected static final String SHOW_ALL_AMENITIES_BUTTON_XPATH="//div[@class='tYfO-toggle-all-button']/button[@class='Iqt3 Iqt3-mod-bold Button-No-Standard-Style Iqt3-mod-variant-outline Iqt3-mod-theme-base Iqt3-mod-shape-rounded-small Iqt3-mod-shape-mod-default Iqt3-mod-spacing-default Iqt3-mod-size-default']";
	protected static final String ALL_AMENITIES_XPATH ="//ul/li[@class='tYfO-amenity']";
	protected static final String ADDRESS_XPATH = "//span[@class='c9fNw-address']";
	protected static final String READ_MORE_BUTTON_XPATH= "/html/body[@class='react react-st en_CA a11y-focus-outlines']/div[@id='root']/div[@class='JjjA']/div[@id='main']/div[@class='kml-layout edges snap BugX']/div[@class='eu4c']/div[5]/div[@class='b40a']/div[@class='b40a-overview']/div[@class='b40a-overview-sections']/div[@class='b40a-section-container b40a-overview-container']/div[@class='b40a-desc-wrap--trunc']/div[@class='b40a-desc-text']/button[@class='b40a-inline-read-more-button']";
	protected static final String OVERVIEW_XPATH = "//div[@class='b40a-desc-wrap--full b40a-expanded']";
	protected static final String INPUT_CITY_XPATH ="//div[@class='NITa NITa-location NITa-hasValue NITa-mod-presentation-expanded']/span[@class='NITa-value']";
	
	
	
	protected MomondoPageObject() {
		
	}
}
