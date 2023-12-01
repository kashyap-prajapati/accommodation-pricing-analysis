package com.accommodation.pricing.analysis.algorithms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.accommodation.pricing.analysis.model.Hotel;

/**
 * InvertedIndex class for creating and searching an inverted index for hotels.
 * @author Aditya
 *
 */

public class InvertedIndex {

	private Map<String,Set<Hotel>> invertedIndexData;
	
	private InvertedIndex() {
		
	}
	
	// Method to add a hotel to the inverted index based on a single key
	private void checkForInvertedIndexPresentOrNot(String key,Hotel hotel) {
		if(key!=null) {
			if(!invertedIndexData.containsKey(key)) {
				Set<Hotel> set = new HashSet<>();
				set.add(hotel);
				invertedIndexData.put(key,set);
			}else {
				Set<Hotel> set = invertedIndexData.get(key);
				set.add(hotel);
				invertedIndexData.put(key,set);
			}
		}
	}
	
	// Method to add a hotel to the inverted index based on an array of keys
	private void checkForInvertedIndexPresentOrNot(String[] keys,Hotel hotel) {
		for(String key : keys) {
			checkForInvertedIndexPresentOrNot(key,hotel);
		}
	}
	
	// Method to add a hotel to the inverted index based on a list of keys
	private void checkForInvertedIndexPresentOrNot(List<String> keys,Hotel hotel) {
		for(String key : keys) {
			checkForInvertedIndexPresentOrNot(key,hotel);
		}
	}
	
	// Public constructor to initialize the inverted index with hotel data
	public InvertedIndex(List<Hotel> hotels) {
		invertedIndexData = new LinkedHashMap<>();
		for(Hotel hotel:hotels) {
			String[] nameWords = hotel.getName().split("\\s");
			checkForInvertedIndexPresentOrNot(nameWords,hotel);
			checkForInvertedIndexPresentOrNot(hotel.getAmenities(), hotel);
			checkForInvertedIndexPresentOrNot(hotel.getAllAmenities(), hotel);
		}
	}
	
	// Method to get hotels from the inverted index based on user input
	public List<Hotel> getInvertedIndexSearchData(String userInput) {
		List<Hotel> list = new ArrayList<>();
		for(String key : invertedIndexData.keySet()){
			if(key.startsWith(userInput)) {
				list.addAll(invertedIndexData.get(key));
			}
		}
		System.out.println("list"+list);
		return list;
	}
	
	
}
