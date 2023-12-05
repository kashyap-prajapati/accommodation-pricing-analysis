package com.accommodation.pricing.analysis.script;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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


/**
 * 
 * MomondoScrapper class contains various methods that will help to scrap the momondo.ca search data.
 * 
 * It contains the  various methods to scrap the data.
 * 
 * @author KASHYAP PRAJAPATI 110126934
 *
 */
@Service
public class MomondoScrapper extends MomondoPageObject {
	
	@Autowired private HotelRepository hotelRepository;
	@Autowired private ApiService apiService;
	
	private static final String MOMONDO_DOMAIN="https://www.momondo.ca/hotel-search";
	private static final String KAYAK_DOMAIN="https://www.ca.kayak.com/hotels";
	private static final String FROM_DATE = "fromDate";
	private static final String TO_DATE = "toDate";
	private static final String PEOPLE = "people";
	private static final String INNER_HTML = "innerHTML";	
	private static final String NAME_CLASS = ".FLpo-big-name";
	private static final String PRICE_CLASS = ".zV27-price";
	private static final String LOCATION_CLASS= ".FLpo-location-name";
	private static final String DESCRIPTION_CLASS = ".FLpo-score-description";
	private static final String SCORE_CLASS = ".FLpo-score";
	private static final String REVIEW_COUNT_CLASS=".FLpo-review-count";
	private static final String HREF="href";
	private static final String NOT_FOUND = "not found";

	
	private HashMap<String,Object> queryParams;
	private List<String> queryString;
	private Driver driver;
	private Trie trie;
	private List<LocationSearch> suggestedLocations;
	
	public MomondoScrapper() {
		this.queryParams = new HashMap<>();
		this.queryString = new LinkedList<>();
	}
	
	
	/**
	 * This method is used to initlize the driver
	 */
	private void initDriver() {
		this.driver = new Driver();	
	}
	
	
	/**
	 * This method take input date from the user and validate it against the YYYY-MM-DD format.
	 * @param scanner object of Scanner class
	 * @return valid date
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
				System.out.println("Please provide date in (YYYY-MM-DD) again : ");
			}
		}
		return strDate;
	}
	
	
	/**
	 * This method take input number from the user and validate it.
	 * @param scanner object of scanner class
	 * @return valid number
	 */
	private Integer takeInputNumberAndValidate(Scanner scanner) {
		String number = null;
		while(number==null) {
			number = scanner.next();
			if(!Validator.checkValidNumber(number))
				number=null;
		}
		return Integer.parseInt(number);
	}
	
	
	/**
	 * This method take input method from the user
	 */
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
		queryParams.put(FROM_DATE, fromDate);
		queryParams.put(TO_DATE, toDate);
		queryParams.put(PEOPLE, adults);
	}
	
	
	/**
	 * This method is used to take location search input from the user.
	 * It also generate trie search tree based on locations so that help in future search to get suggested location.
	 *  
	 */
	private void takeSearchInput() {
		Scanner scanner = new Scanner(System.in);
		suggestedLocations = apiService.getSearchSuggestionsFromDb("1");
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
			System.out.println(i+". Oops!! No result found. do you want to search again from cache? ");
			System.out.println((i+1)+". New search");
			optInput = scanner.nextInt();
			if(optInput==i)
				continue;
			if(optInput==i+1)
				break;
			searchQueryMap = suggestedLocation.get(optInput-1);
		}
		
		optInput=0;
		// if location is not available inside the database fetch the location from the api and store it into the database.
		while(searchQueryMap==null) {
			System.out.println("Please enter location ");
			String str = scanner.next();
			List<LocationSearch> sugestionList = apiService.getSearch(str);
			int i=1;
			System.out.println("did you mean these ? Please select one");
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
	
	
	/**
	 * This method is used to getURL based on type 
	 * @param type 1: KAYAK domain, 2: Momondo domain
	 * @return full generated url
	 */
	private String getURL(int type) {
		URLParams urlParams = new URLParams(queryString);
		return  (type == 1 ? KAYAK_DOMAIN : MOMONDO_DOMAIN) + urlParams.generateQueryString();
	}
		
	
	/**
	 * This is the start method of the momondo scrapper.
	 * This method first prepare the URL navigate to that url.
	 * It also take the input from the user to generate the url.
	 *  It traverse through all pages of the momondo scrapper.
	 *  It also traverse through all the link displayed and click on each link that will open page in new windows.
	 * @param type 1: KAYAK domain, 2: Momondo domain
	 */
	public void start(int type) {
		takeSearchInput();
		initDriver();
		driver.navigateToUrl(getURL(type));
		int totalNumberPage = Integer.parseInt(driver.waitForElementToVisible(LAST_PAGE_NUMBER_XPATH).getText());
		String city = driver.waitForElementToVisible(INPUT_CITY_XPATH).getText();
		String fromDate = queryParams.get(FROM_DATE).toString();
		String toDate = queryParams.get(TO_DATE).toString();
		String people = queryParams.get(PEOPLE).toString();
	
		for(int page=0;page<totalNumberPage;page++) {
			List<WebElement> hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
			for(int hotelIndex=0;hotelIndex<hotelPageList.size();hotelIndex++) {
				try {
					hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
					List<WebElement> hotelNameLinkList = driver.findElements(HOTEL_NAME_LINK_XPATH);
					WebElement hotelPage = hotelPageList.get(hotelIndex);
					Document document = HtmlUtil.parse(hotelPage.getAttribute(INNER_HTML)); 
					String name = HtmlUtil.getFirstElementText(document, NAME_CLASS);
					String price = HtmlUtil.getFirstElementText(document, PRICE_CLASS);
					String location = HtmlUtil.getFirstElementText(document, LOCATION_CLASS);
					String reviewDescription= HtmlUtil.getFirstElementText(document, DESCRIPTION_CLASS);
					String score =  HtmlUtil.getFirstElementText(document, SCORE_CLASS);
					String reviewCount =  HtmlUtil.getFirstElementText(document, REVIEW_COUNT_CLASS);
					String url = hotelNameLinkList.get(page).getAttribute(HREF);
				    hotelNameLinkList.get(hotelIndex).click();
				    
				    ArrayList<String> tabs = new ArrayList<>(driver.getDriver().getWindowHandles());
				    driver.switchToTab(tabs.get(1));
				    
					String overView = NOT_FOUND;
					
					if(driver.isElementDisplayed(READ_MORE_BUTTON_XPATH)) {
						driver.clickOnWebElement(READ_MORE_BUTTON_XPATH);		
						if(driver.isElementDisplayed(OVERVIEW_XPATH))
							overView = driver.waitForElementToVisible(OVERVIEW_XPATH).getText();
					}
					
					String address = NOT_FOUND;
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
					 driver.closeCurrentAndSwitchToParent();
					 hotelPageList = driver.findElements(HOTEL_LIST_XPATH);
					 
				}
			}
			driver.clickOnWebElement(NEXT_BUTTON_XPATH);
		}
		driver.close();
	}

}
