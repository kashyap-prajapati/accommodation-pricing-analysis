package com.accommodation.pricing.analysis.script;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.accommodation.pricing.analysis.algorithms.Trie;
import com.accommodation.pricing.analysis.constant.Constants;
import com.accommodation.pricing.analysis.driver.Driver;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.model.URLParams;
import com.accommodation.pricing.analysis.pageobjects.VerboPageObject;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.service.ApiService;
import com.accommodation.pricing.analysis.validator.Validator;


/**
 * 
 * VerboScrapper class is used to scrap the www.verbo.com
 * This class contains the various methods that is used to extract the data from the  verbo.com site.
 * This class take input from the user and prepare URL to scrap the data.
 * This class also integrate with MONGODB to store the location search data.
 * Also implements TRIE tree data structure to get suggestion based on already suggested words.
 * @author KASHYAP PRAJAPATI 110126934
 *
 */
@Component
public class VerboScrapper extends VerboPageObject {
	
	// Constants for URL parameters
	private static final String DOMAIN_NAME = "https://www.vrbo.com/search";
	private static final String START_DATE = "startDate";
	private static final String END_DATE = "endDate";
	private static final String ROOMS = "rooms";
	private static final String ADULTS = "adults";
	private static final String CHILDREN="children";
	private static final String SORT="sort";
	private static final String RECOMMENDED="RECOMMENDED";
	private static final String DESTINATION="destination";
	private static final String REGION_ID="regionId";

	@Autowired private HotelRepository hotelRepository;
	private ApiService apiService;
	
	private HashMap<String,Object> queryParams;
	private Driver driver;	
	private Trie trie;
	private List<LocationSearch> suggestedLocations;
	
	
	/**
     * Constructor for VerboScrapper.
     * Initializes instance variables and sets up TRIE with suggested words.
     *
     * @param apiService The ApiService dependency.
     */
	@Autowired
	public VerboScrapper(ApiService apiService) {
		this.queryParams = new LinkedHashMap<>();
		this.apiService = apiService;
		suggestedLocations = apiService.getSearchSuggestionsFromDb("2");
		List<String> words = new ArrayList<>();
		for(LocationSearch locationSearch:suggestedLocations) {
			words.add(locationSearch.getSearchQuery());
		}
		trie = new Trie(words);	
	
	}
	
	/**
     * Web Driver Initialization
     */
	private void initDriver() {
		this.driver = new Driver();	
	}
	
	/**
     * Handles the user input, prepares the URL, and initiates the scraping process.
     */
	private String takeInputDateAndValidate(Scanner scanner) {
		String strDate=null;
		while(strDate==null) {
			try {
				strDate = scanner.next();
				SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT);
				df.setLenient(false);
				Date date =  df.parse(strDate);
				if(!date.after(new Date())) {
					System.out.println(" Date should be greater than today's date");
					strDate = null;
					continue;
				}
			} catch (ParseException ex) {
				strDate = null;
				System.out.println("Error : Invalid date");
				System.out.println(" Please provide date in (YYYY-MM-DD) again : ");
			}
		}
		return strDate;
	}
	// Method to take a valid number input from the user
	private Integer takeInputNumberAndValidate(Scanner scanner) {
		String number = null;
		while(number==null) {
			number = scanner.next();
			if(!Validator.checkValidNumber(number))
				number=null;
		}
		return Integer.parseInt(number);
	}
	
	// Method to take user input for date, number of rooms, and number of adults
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
		queryParams.put(START_DATE, fromDate);
		queryParams.put(END_DATE, toDate);
		queryParams.put(ROOMS, rooms);
		queryParams.put(ADULTS, adults);
		queryParams.put(CHILDREN, 0);
		queryParams.put(SORT, RECOMMENDED);
		
	
	}
	
	// Method to take user input for the search query, handle suggestions, and set destination parameters
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
		queryParams.put(DESTINATION,searchQueryMap.getFullName());
		queryParams.put(REGION_ID, searchQueryMap.getRegionId());
		takeInput();
	}
	
	// Method to construct the URL based on the user input parameters
	private String getURL() {
		URLParams urlParams = new URLParams(queryParams);
		return DOMAIN_NAME+urlParams.generateQueryParams();
	}
	
	// Method to handle the user input, initialize the WebDriver, and navigate to the constructed URL
	private void handleInput() {
		takeSearchInput();
		initDriver();
		driver.navigateToUrl(getURL()); 		
	}
	
	// Main method to start fpr process of scrapping
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
				    hotel.setFromDate(queryParams.get(START_DATE).toString());
				    hotel.setToDate(queryParams.get(END_DATE).toString());
				    hotel.setNoOfguests(queryParams.get(ADULTS).toString());
				    hotelRepository.save(hotel);
					
				}catch (Exception e) {
					e.printStackTrace();
				}finally {
					 driver.closeCurrentAndSwitchToParent();
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
