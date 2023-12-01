package com.accommodation.pricing.analysis.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accommodation.pricing.analysis.algorithms.HeapSort;
import com.accommodation.pricing.analysis.algorithms.InvertedIndex;
import com.accommodation.pricing.analysis.algorithms.KMP;
import com.accommodation.pricing.analysis.algorithms.EditDistance;
import com.accommodation.pricing.analysis.feignclient.ApiFeignClient;
import com.accommodation.pricing.analysis.feignclient.VerboFeignClient;
import com.accommodation.pricing.analysis.model.Hotel;
import com.accommodation.pricing.analysis.model.LocationSearch;
import com.accommodation.pricing.analysis.model.VerboSearch;
import com.accommodation.pricing.analysis.model.VerboSearch.HotwireSuggestion;
import com.accommodation.pricing.analysis.repository.HotelRepository;
import com.accommodation.pricing.analysis.repository.LocationSearchRepository;

@Service

/**
 * Service class for handling API calls and business logic.
 * @author kashyap prajapati
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
	
	// Get a list of words from the database and suggest corrections using Edit Distance
	public List<Hotel> getWordListfromDB(){
		List<Hotel> obj= hotelRepository.findAll();
		Scanner user_input_parser = new Scanner(System.in);
		ArrayList<String> hotel_valid_words = new ArrayList<String>();
		for(Hotel hotel: obj) {
			String hotel_name = hotel.getName();
			//System.out.println(hotel_name);
			hotel_valid_words.add(hotel_name);
			
		}
		EditDistance lcs_obj= new EditDistance();
		System.out.print("Enter the hotel name: ");
		String user_provided_word = user_input_parser.next();
		lcs_obj.suggest_corrections(user_provided_word,hotel_valid_words,5);
		
		return obj;
	}
	
	// Perform a search using Knuth-Morris-Pratt algorithm and print the result
	public void implementKPM(String patt) {
		List<Hotel> obj= hotelRepository.findAll();
		KMP kmp = new KMP();
		String text = "";
		for(Hotel hotel : obj) {
			text = hotel.getOverView();
			int i = kmp.searchKMP(patt, text);
			if(i!=-1) {
				printHotels(Arrays.asList(hotel));
				break;
			}
		}
		
	}
	
	// Get a sorted list of hotels based on prices from the database
	public Map<Integer, String> getHotelListfromDB(){
		Scanner user_input_hotel_reader = new Scanner(System.in);
		List<Hotel> obj= hotelRepository.findAll();
		Map<Integer, String> hotelPriceMap = new HashMap<>();
		for(Hotel hotel: obj) {
			Integer hotel_price =  Integer.valueOf(hotel.getPrice());
			String hotel_id = hotel.getId();
			hotelPriceMap.put(hotel_price,hotel_id);
		}
			System.out.println(hotelPriceMap);	
			
			 // Convert the entries of the map to a list
	        List<Map.Entry<Integer, String>> entryList = new ArrayList<>(hotelPriceMap.entrySet());

	        // Use heap sort to sort the entries based on keys
	        HeapSort sort_hotels_on_prices = new HeapSort();
	        sort_hotels_on_prices.sortHotelPriceFuction(entryList);

	        // Print the sorted entries
	        System.out.println("Sorted Map:");
	        for (Map.Entry<Integer, String> entry : entryList) {
	            //System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
	            //List<Hotel> sorted_hotels= 
	            System.out.println(hotelRepository.findById(entry.getValue()));
	        	
	        }

			
			return hotelPriceMap;
		}
	

}
