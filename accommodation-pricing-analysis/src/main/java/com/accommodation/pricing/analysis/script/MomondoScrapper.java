package com.accommodation.pricing.analysis.script;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.algorithms.Trie;
import com.accommodation.pricing.analysis.constant.Constants;
import com.accommodation.pricing.analysis.driver.Driver;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.pageobjects.MomondoPageObject;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.service.ApiService;
import com.accommodation.pricing.analysis.util.HtmlUtil;
import com.accommodation.pricing.analysis.validator.Validator;

@Service
public class MomondoScrapper extends MomondoPageObject {
	
	@Autowired private HotelRepository hotelRepository;
	@Autowired private ApiService apiService;
	
	private static final String ONEFINESTAY_DOMAIN="https://www.momondo.ca/hotel-search";
	private HashMap<String,Object> queryParams;
	private List<String> queryString;
	private Driver driver;
	private Trie trie;
	private List<LocationSearch> suggestedLocations;
	
	public MomondoScrapper() {
		this.queryParams = new HashMap<>();
		this.queryString = new LinkedList<>();
	}
	
	private void initDriver() {
		this.driver = new Driver(false);	
	}
	
	private String takeInputDateAndValidate(Scanner scanner) {
		String strDate=null;
		while(strDate==null) {
			try {
				strDate = scanner.next();
				new SimpleDateFormat(Constants.DATE_FORMAT).parse(strDate);
			} catch (ParseException ex) {
				strDate = null;
				System.out.println("Please provide date in (YYYY-MM-DD) : ");
			}
		}
		return strDate;
	}
	
	private Integer takeInputNumberAndValidate(Scanner scanner) {
		String number = null;
		while(number==null) {
			number = scanner.next();
			if(!Validator.checkValidNumber(number))
				number=null;
		}
		return Integer.parseInt(number);
	}
	
	private void takeInput() {
		System.out.flush();
		Scanner scanUserInput = new Scanner(System.in);
		System.out.println("Please provide from date (YYYY-MM-DD) : ");
		String fromDate = takeInputDateAndValidate(scanUserInput);
		System.out.println("Please provide to date (YYYY-MM-DD) : ");
		String toDate = takeInputDateAndValidate(scanUserInput);
		System.out.println("Please enter number of adults");
		Integer adults = takeInputNumberAndValidate(scanUserInput);
		queryString.add(fromDate);
		queryString.add(toDate);
		queryString.add(adults+"adults");
		queryParams.put("fromDate", fromDate);
		queryParams.put("toDate", toDate);
		queryParams.put("people", adults);
		queryParams.put("page", 1);
	
	}
	
	private void takeSearchInput() {
		Scanner scanner = new Scanner(System.in);
		suggestedLocations = apiService.getSearchSuggestionsFromDb();
		List<String> words = new ArrayList<>();
		for(LocationSearch locationSearch:suggestedLocations) {
			words.add(locationSearch.getSearchQuery());
		}
		trie = new Trie(words);	
	
		LocationSearch searchQueryMap = null;
		int optInput=0;
		System.out.println("Lookout the already entered location");
		while(searchQueryMap==null) {
			System.out.println("Please enter the word to get suggestions");
			List<String> suggestedList = trie.getSuggestion(scanner.next());
			
		    List<LocationSearch> suggestedLocation = suggestedLocations.stream().filter(item->suggestedList.contains(item.getSearchQuery())).collect(Collectors.toList());
		    int i=1;
			System.out.println("did you mean ? Please select one");
			for(LocationSearch suggestion : suggestedLocation) {
				System.out.println((i++)+". "+suggestion.getDisplayname());
			}
			System.out.println(i+". Oops!! No result found. Search again ");
			System.out.println((i+1)+". New search");
			optInput = scanner.nextInt();
			if(optInput==i)
				continue;
			if(optInput==i+1)
				break;
			searchQueryMap = suggestedLocation.get(optInput-1);
		}
		
		optInput=0;
		while(searchQueryMap==null) {
			System.out.println("Please enter location ");
			String str = scanner.next();
			List<LocationSearch> sugestionList = apiService.getSearch(str);
			int i=1;
			System.out.println("did you mean ? Please select one");
			for(LocationSearch suggestion : sugestionList) {
				System.out.println((i++)+". "+suggestion.getDisplayname());
			}
			System.out.println(i+". Search again ");
			optInput = scanner.nextInt();
			if(optInput==i)
				continue;
			searchQueryMap = sugestionList.get(optInput-1);
		}
		queryString.add(Validator.removeWhiteSpace(searchQueryMap.getDisplayname())+"-c"+searchQueryMap.getCtid());
		queryParams.put("searchQuery",searchQueryMap.getDisplayname());

		takeInput();		
	}
	
	private String getURL() {
		URLParams urlParams = new URLParams(queryString);
		return ONEFINESTAY_DOMAIN+urlParams.generateQueryString();
	}
	
	public void start1() {
	
		takeSearchInput();
	}
	
	public void start() {
		takeSearchInput();
		initDriver();

		driver.navigateToUrl(getURL());
		int totalNumberPage = Integer.parseInt(driver.waitForElementToVisible(LAST_PAGE_NUMBER_XPATH).getText());
		String city = driver.waitForElementToVisible(INPUT_CITY_XPATH).getText();
		String fromDate = queryParams.get("fromDate").toString();
		String toDate = queryParams.get("toDate").toString();
		String people = queryParams.get("people").toString();
	
		for(int page=0;page<totalNumberPage;page++) {
			List<WebElement> hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
			for(int hotelIndex=0;hotelIndex<hotelPageList.size();hotelIndex++) {
				try {
					hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
					List<WebElement> hotelNameLinkList = driver.findElements(HOTEL_NAME_LINK_XPATH);
					WebElement hotelPage = hotelPageList.get(hotelIndex);
					Document document = HtmlUtil.parse(hotelPage.getAttribute("innerHTML")); 

					String name = HtmlUtil.getFirstElementText(document, ".FLpo-big-name");
					String price = HtmlUtil.getFirstElementText(document, ".zV27-price");
					String location = HtmlUtil.getFirstElementText(document, ".FLpo-location-name");
					String reviewDescription= HtmlUtil.getFirstElementText(document, ".FLpo-score-description");
					String score =  HtmlUtil.getFirstElementText(document, ".FLpo-score");
					String reviewCount =  HtmlUtil.getFirstElementText(document, ".FLpo-review-count");
				    
					String url = hotelNameLinkList.get(page).getAttribute("href");
				    hotelNameLinkList.get(hotelIndex).click();
				    
				    ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
				    driver.switchToTab(tabs.get(1));
				    

					String overView = "not found";
					
					if(driver.isElementDisplayed(READ_MORE_BUTTON_XPATH)) {
						driver.clickOnWebElement(READ_MORE_BUTTON_XPATH);		
						if(driver.isElementDisplayed(OVERVIEW_XPATH))
							overView = driver.waitForElementToVisible(OVERVIEW_XPATH).getText();
					}
					
					
					String address = "not found";
					if(driver.isElementDisplayed(ADDRESS_XPATH))
						address = driver.waitForElementToVisible(ADDRESS_XPATH).getText();
					
				
					List<String> amenitiesList = new ArrayList<>();
				    
				    if(driver.isElementDisplayed(AMENITIES_XPATH)) {
				    	List<WebElement> amenitiesElement = driver.findElements(AMENITIES_XPATH);
						for(WebElement  amenityElement :amenitiesElement) {
							amenitiesList.add(amenityElement.getText());
						}
				    }
				  
				    List<String> allAmenitiesList = new ArrayList<>();
				    if(driver.isElementDisplayed(SHOW_ALL_AMENITIES_BUTTON_XPATH)) {
				    	driver.clickOnWebElement(SHOW_ALL_AMENITIES_BUTTON_XPATH);
				    	List<WebElement> amenitiesElement = driver.findElements(ALL_AMENITIES_XPATH);
						for(WebElement  amenityElement :amenitiesElement) {
							allAmenitiesList.add(amenityElement.getText());
						}
				    }
					
					
					Hotel hotel = new Hotel();
				    hotel.setName(name);
				    hotel.setPrice(price);
				    hotel.setLocation(location);
				    hotel.setUrl(url);
				    hotel.setReviewDescription(reviewDescription);
				    hotel.setScore(score);
				    hotel.setReviewCount(reviewCount);
				  
				    hotel.setCity(city);
				    hotel.setFromDate(fromDate);
				    hotel.setToDate(toDate);
				    hotel.setNoOfguests(people);
				    
				    hotel.setOverView(overView);
				    hotel.setAddress(address);
				    hotel.setAmenities(amenitiesList);
				    hotel.setAllAmenities(allAmenitiesList);
				    
				    hotelRepository.save(hotel);
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					 ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
					 if(tabs.size()>1) {
						 driver.getDriver().close();
						 driver.getDriver().switchTo().window(tabs.get(0));
					 }
					 hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
				}
			}
			driver.clickOnWebElement(NEXT_BUTTON_XPATH);
			break;
		}
	//	driver.close();
	}

}
