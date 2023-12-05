package com.accommodation.pricing.analysis.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.algorithms.EditDistance;
import com.accommodation.pricing.analysis.algorithms.HeapSort;
import com.accommodation.pricing.analysis.algorithms.InvertedIndex;
import com.accommodation.pricing.analysis.algorithms.KMP;
import com.accommodation.pricing.analysis.algorithms.PageRanking;
import com.accommodation.pricing.analysis.feignclient.ApiFeignClient;
import com.accommodation.pricing.analysis.feignclient.VerboFeignClient;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.model.VerboSearch;
import com.accommodation.pricing.analysis.model.VerboSearch.HotwireSuggestion;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.repository.LocationSearchRepository;
import com.accommodation.pricing.analysis.validator.Validator;

@Service

/**
 * Service class for handling API calls and business logic.
 * @author sachreet kaur(110122441)
 *
 */
public class ApiService {

	@Autowired private LocationSearchRepository locationSearchRepository;
	@Autowired private ApiFeignClient apiFeignClient;
	@Autowired private VerboFeignClient hotwireFeignClient;
	private HotelRepository hotelRepository;
	
	private InvertedIndex invertedIndex;
	
	@Autowired 
	public ApiService(HotelRepository hotelRepository) {
		this.hotelRepository=hotelRepository;
		invertedIndex = new InvertedIndex(hotelRepository.findAll());
	}
	
	// Check if LocationSearch already exists in the database
	public boolean isLocationSearchAlreadyExists(LocationSearch locationSearch) {
		return locationSearchRepository.getLocationSearchs(locationSearch.getCtid(),locationSearch.getCategory()).isEmpty();
	}
	
	// Get search suggestions from the database based on category
	public List<LocationSearch> getSearchSuggestionsFromDb(String category){
		return locationSearchRepository.findByCategory(category);
	}
	
	// Get search suggestions from an external API and update the database
	public List<LocationSearch> getSearch(String search){
		List<LocationSearch> list =  apiFeignClient.getSearchDropdown(search);
		for(LocationSearch locationSearch : list) {
			if(isLocationSearchAlreadyExists(locationSearch)) {
				locationSearch.setCategory("1");
				locationSearch.setSearchQuery(locationSearch.getDisplayname());
				locationSearchRepository.insert(locationSearch);	
			}
		}
		return list;
	}
	
	// Get search suggestions from an external API (Hotwire) and update the database
	public List<LocationSearch> getSerachForVerbo(String search){
		VerboSearch hotwireSearch = hotwireFeignClient.getSearchDropdownForHotwire(search);
		List<LocationSearch> list = new ArrayList<>();
		for(HotwireSuggestion hotwireSuggestion:hotwireSearch.getSr()) {
			LocationSearch locationSearch = new LocationSearch();
			locationSearch.setCid(hotwireSuggestion.getGaiaId());
			locationSearch.setDisplayname(hotwireSuggestion.getRegionNames().getDisplayName());
			locationSearch.setRegionId(hotwireSuggestion.getGaiaId());
			locationSearch.setCategory("2");
			locationSearch.setSearchQuery(locationSearch.getDisplayname());
			locationSearch.setFullName(hotwireSuggestion.getRegionNames().getFullName());
			if(isLocationSearchAlreadyExists(locationSearch)) {
				locationSearchRepository.insert(locationSearch);
			}
			list.add(locationSearch);
		}
		return list;
	}
	
	// Perform a search using Inverted Index and print the results
	public void implementInvertedIndexSearch(String str) {
		printHotels(invertedIndex.getInvertedIndexSearchData(str));
	}
	
	// Printy hotel details
	public void printHotels(List<Hotel> hotels) {
		int i=1;
		for(Hotel hotel: hotels) {
			System.out.flush();
			System.out.println("\n\n");
			System.out.println("|==================================================================|");
			System.out.println(" |   Name     : "+hotel.getName());
			System.out.println(" |   Overview : "+ Validator.removeSpecialCharacterFromText(hotel.getOverView()));
			System.out.println(" |   Price    : "+hotel.getPrice());
			System.out.println(" |   Address  : "+hotel.getAddress());
			System.out.println(" |   City     : "+hotel.getCity());
			System.out.println(" |   Score    : "+hotel.getScore());
			System.out.println(" |   Review   : "+hotel.getReviewCount());
			System.out.println(" |   Amenties : "+hotel.getAmenities());
			System.out.println(" |==================================================================|");
		}
	}
	
	// Get hotels by City from DB
	public List<Hotel> getHotelByCity(String city){
		return hotelRepository.getHotelByCity(city);
	}
	
	// Get hotels by Name from DB
	public List<Hotel> getHotelByName(String name){
		return hotelRepository.getHotelByName(name);
	}

	
	// Perform a search using Knuth-Morris-Pratt algorithm and print the result
	public void implementKPM(String patt) {
		List<Hotel> obj= hotelRepository.findAll();
		KMP kmp = new KMP();
		String text = "";
		for(Hotel hotel : obj) {
			text = hotel.getOverView().toLowerCase();
			int i = kmp.searchKMP(patt.toLowerCase(), text);
			if(i!=-1) {
				printHotels(Arrays.asList(hotel));
				break;
			}
		}
		
	}
	
	
	// Get a list of words from the database and suggest corrections using Edit Distance
	public List<Hotel> getWordListfromUsingEditDistance(String userInput){
		List<Hotel> obj= hotelRepository.findAll();
		ArrayList<String> validWords = new ArrayList<>();
		for(Hotel hotel: obj) {
			validWords.add(hotel.getName());
		}
		EditDistance lcs= new EditDistance();
		lcs.suggest_corrections(userInput,validWords,5);
		return obj;
	}
	


	public void getHotelListUsingHeapSort(){
		List<Hotel> obj= hotelRepository.findAll();
		Map<Integer, Hotel> hotelPriceMap = new HashMap<>();
		for(Hotel hotel: obj) {
			try {
				Integer hotelPrice =  Integer.valueOf(hotel.getPrice());
				hotelPriceMap.put(hotelPrice,hotel);
			}catch(Exception e){
				
			}
		}
		List<Map.Entry<Integer, Hotel>> entryList = new ArrayList<>(hotelPriceMap.entrySet());
        HeapSort heapSort = new HeapSort();
        heapSort.sortHotelPriceFuction(entryList);
        List<Hotel> hotelList = new ArrayList<Hotel>();
        for (Map.Entry<Integer, Hotel> entry : entryList) {            
        	hotelList.add(entry.getValue());
        }
        printHotels(hotelList);
	}
	
	
	public void implementPageRanking() {
		List<Hotel> hotelList = hotelRepository.findAll();
		PageRanking pageRanking = new PageRanking();
		pageRanking.implementPageRank(hotelList);
	}
	

}
