package com.accommodation.pricing.analysis.script;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.algorithms.Trie;
import com.accommodation.pricing.analysis.constant.Constants;
import com.accommodation.pricing.analysis.driver.Driver;
import com.accommodation.pricing.analysis.feignclient.HotwireFeignClient;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.model.HotwireSearch.HotwireSuggestion;
import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.pageobjects.HotwirePageObject;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.service.ApiService;
import com.accommodation.pricing.analysis.validator.Validator;

@Component
public class HotwireScrapper extends HotwirePageObject {
	
	private static final String DOMAIN_NAME = "https://www.vrbo.com/search";

	@Autowired private HotelRepository hotelRepository;
	private ApiService apiService;
	
	private HashMap<String,Object> queryParams;
	private Driver driver;	
	private Trie trie;
	private List<LocationSearch> suggestedLocations;
	
	
	@Autowired
	public HotwireScrapper(ApiService apiService) {
		this.queryParams = new LinkedHashMap<>();
		this.apiService = apiService;
		suggestedLocations = apiService.getSearchSuggestionsFromDb("2");
		List<String> words = new ArrayList<>();
		for(LocationSearch locationSearch:suggestedLocations) {
			words.add(locationSearch.getSearchQuery());
		}
		trie = new Trie(words);	
	
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
		System.out.println("Please enter number of rooms");
		Integer adults = takeInputNumberAndValidate(scanUserInput);
		System.out.println("Please enter number of adults");
		Integer rooms = takeInputNumberAndValidate(scanUserInput);
		queryParams.put("startDate", fromDate);
		queryParams.put("endDate", toDate);
		queryParams.put("rooms", rooms);
		queryParams.put("adults", adults);
		queryParams.put("children", 0);
		queryParams.put("sort", "RECOMMENDED");
		
	
	}
	
	
	private void takeSearchInput() {
		Scanner scanner = new Scanner(System.in);
	
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
			System.out.println(i+". do you want to search again from cache? ");
			System.out.println((i+1)+". Oops!! No result found. New search");
			optInput = scanner.nextInt();
			if(optInput==i)
				continue;
			if(optInput==i+1)
				break;
			searchQueryMap = suggestedLocation.get(optInput-1);
		}
		
		while (searchQueryMap==null) {
			System.out.println("Please enter location ");
			String str = scanner.next();
			List<LocationSearch> hotwireList = apiService.getSerachForVerbo(str);
			int i=1;
			System.out.println("did you mean these ? Please select one");
			for(LocationSearch suggestion : hotwireList) {
				System.out.println((i++)+". "+suggestion.getDisplayname());
			}
			System.out.println(i+". Search again ");
			optInput = scanner.nextInt();
			if(optInput==i)
				continue;
			searchQueryMap = hotwireList.get(optInput-1);
		}		
		queryParams.put("destination",searchQueryMap.getFullName());
		queryParams.put("regionId", searchQueryMap.getRegionId());
		takeInput();
	}
	
	
	private String getURL() {
		URLParams urlParams = new URLParams(queryParams);
		return DOMAIN_NAME+urlParams.generateQueryParams();
	}
	
	
	private void handleInput() {
		takeSearchInput();
		initDriver();
		driver.navigateToUrl(getURL()); 		
	}
	public void start() {
		handleInput();
		
		List<WebElement> list = driver.findElements(HOTEL_LIST_XPATH);

		
		int i=0;
		int total = list.size();
			while(i<total) {
				try {
					WebElement hyperlikeElement = list.get(i);	
					hyperlikeElement.click();
				
				    ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
				    driver.switchToTab(tabs.get(1)); 
				    
				    String hotelName = null;
				    String price = null;
				    String location = null;
				    String reviewDescription= null;
				    String score = null;
				    String reviewCount = null;
				    String city = null;
				    String overView=null;
				    
				    if(driver.isElementDisplayed(HOTEL_NAME_XPATH))
				    	hotelName = driver.waitForElementToVisible(HOTEL_NAME_XPATH).getText();
				    
					if(driver.isElementDisplayed(HOTEL_PRICE_XPATH)) {
						price = driver.waitForElementToVisible(HOTEL_PRICE_XPATH).getText();
					}
						
				    List<String> amenitiesList = new ArrayList<>();
					if(driver.isElementDisplayed(TOP_AMENITIES_XPATH)) {
				    	List<WebElement> amenitiesElement = driver.findElements(TOP_AMENITIES_XPATH);
						for(WebElement  amenityElement :amenitiesElement) {
							amenitiesList.add(amenityElement.getText());
						}
				    }
					
					driver.clickOnWebElement(AMENITIES_TAB_XPATH);
					
					if(driver.isElementDisplayed(OVERVIEW_XPATH))
				    	overView=driver.waitForElementToVisible(OVERVIEW_XPATH).getText();
					
					List<String> allAmenitiesList = new ArrayList<>();
					
				    if(driver.isElementDisplayed(SEE_ALL_AMENITIES_BUTTON_XPATH)) {
				    	driver.clickOnWebElement(SEE_ALL_AMENITIES_BUTTON_XPATH);
				    	if(driver.isElementDisplayed(ALL_AMENITIES_XPATH)) {
				    		List<WebElement> amenitiesElement = driver.findElements(ALL_AMENITIES_XPATH);
							for(WebElement  amenityElement :amenitiesElement) {
								allAmenitiesList.add(amenityElement.getText());
							}
				    	}	
						if(driver.isElementDisplayed(POP_UP_AMENITIES_CLOSE_BUTTON_XPATH))
							driver.clickOnWebElement(POP_UP_AMENITIES_CLOSE_BUTTON_XPATH);
				    }
				    
				    driver.clickOnWebElement(LOCATION_TAB_XPATH);
				    
				    if(driver.isElementDisplayed(CITY_XPATH)) {
				    	city = driver.waitForElementToVisible(CITY_XPATH).getText();
				    }
				    
					if(driver.isElementDisplayed(HOTEL_LOCATION_XPATH))
						location = driver.waitForElementToVisible(HOTEL_LOCATION_XPATH).getText();
					
				    driver.clickOnWebElement(LAST_TAB_XPATH);
				    
					if(driver.isElementDisplayed(REVIEW_DESCRIPTION_XPATH))
						reviewDescription = driver.waitForElementToVisible(REVIEW_DESCRIPTION_XPATH).getText();
					
					if(driver.isElementDisplayed(REVIEW_SCORE_XPATH))
						score = driver.waitForElementToVisible(REVIEW_SCORE_XPATH).getText();
					
					if(driver.isElementDisplayed(REVIEW_COUNT_XPATH)) {
						reviewCount = driver.waitForElementToVisible(REVIEW_COUNT_XPATH).getText();
					}
				    
				    Hotel hotel = new Hotel();
				    hotel.setName(hotelName);
				    hotel.setPrice(price);
				    hotel.setReviewCount(reviewCount);
				    hotel.setScore(score);
				    hotel.setReviewDescription(reviewDescription);
				    hotel.setAllAmenities(allAmenitiesList);
				    hotel.setAmenities(amenitiesList);
				    hotel.setLocation(location);
				    hotel.setOverView(overView);
				    hotel.setCity(city);
				    hotel.setFromDate(queryParams.get("startDate").toString());
				    hotel.setToDate(queryParams.get("endDate").toString());
				    hotel.setNoOfguests(queryParams.get("adults").toString());
				    hotelRepository.save(hotel);
					
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
					if(tabs.size()>1) {
						 driver.getDriver().close();
						 driver.getDriver().switchTo().window(tabs.get(0));
					} 
					if(i+1==total) {
						JavascriptExecutor js = (JavascriptExecutor) driver.getDriver();
						js.executeScript("window.scrollBy(0,document.body.scrollHeight)", "");
						break;
					}
					list = driver.findElements(HOTEL_LIST_XPATH);
					total = list.size();
					i++;
				}

			}
		
		
	}
	
}
